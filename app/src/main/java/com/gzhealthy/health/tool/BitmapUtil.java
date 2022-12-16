package com.gzhealthy.health.tool;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class BitmapUtil {
    /**
     * 1)软引用 ,已经不适合缓存图片信息,加载图片时会出现重叠的现象
     * 2)Android 3.0 (API Level 11)中，图片的数据会存储在本地的内存当中
     * 因而无法用一种可预见的方式将其释放，这就有潜在的风险造成应用程序的内存溢出并崩溃，
     * 3)因为从 Android 2.3 (API Level 9)开始，垃圾回收器会更倾向于回收持有软引用或弱引用的对象，
     * 这让软引用和弱引用变得不再可靠。
     */
    private static Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 初始化lrucache,最少使用最先移除,LruCache来缓存图片，
     * 当存储Image的大小大于LruCache设定的值，系统自动释放内存，
     */
    private static LruCache<String, Bitmap> mMemoryCache;

    static {
        final int memory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = memory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap value) {
                // return value.getByteCount() / 1024;
                return value.getHeight() * value.getRowBytes();
            }
        };
    }
    // ---lrucache----------------------------------------------------

    /**
     * 添加图片到lrucache
     *
     * @param key
     * @param bitmap
     */
    public synchronized void addBitmapToMemCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            if (key != null & bitmap != null) {
                mMemoryCache.put(key, bitmap);
            }
        }
    }

    /**
     * 清除缓存
     */
    public void clearMemCache() {
        if (mMemoryCache != null) {
            if (mMemoryCache.size() > 0) {
                mMemoryCache.evictAll();
            }
            mMemoryCache = null;
        }
    }

    /**
     * 移除缓存
     */
    public synchronized void removeMemCache(String key) {
        if (key != null) {
            if (mMemoryCache != null) {
                Bitmap bm = mMemoryCache.remove(key);
                if (bm != null)
                    bm.recycle();
            }
        }
    }

    /**
     * 从lrucache里读取图片
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key) {
        if (key != null) {
            return mMemoryCache.get(key);
        }
        return null;
    }

    /**
     * 加载图片
     *
     * @param context
     * @param resId
     * @param imageView
     */
    public void loadBitmap(Context context, int resId, ImageView imageView) {
        final String imageKey = String.valueOf(resId);
        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(resId);
            BitmapWorkerTask task = new BitmapWorkerTask(context);
            task.execute(resId);
        }
    }

    /**
     * 任务类
     *
     * @author chenlin
     * @version 1.0
     * @Project App_View
     * @Package com.android.view.tool
     * @Date 2014年5月10日
     */
    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private Context mContext;

        public BitmapWorkerTask(Context context) {
            mContext = context;
        }

        // 在后台加载图片。
        @Override
        protected Bitmap doInBackground(Integer... params) {
            final Bitmap bitmap = decodeSampledBitmapFromResource(mContext.getResources(), params[0], 100, 100);
            addBitmapToMemCache(String.valueOf(params[0]), bitmap);
            return bitmap;
        }
    }

    // --软引用---------------------------------------------------------
    public static void addBitmapToCache(String path) {
        // 强引用的Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        // 软引用的Bitmap对象
        SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
        // 添加该对象到Map中使其缓存
        imageCache.put(path, softBitmap);
    }

    public static Bitmap getBitmapByPath(String path) {
        // 从缓存中取软引用的Bitmap对象
        SoftReference<Bitmap> softBitmap = imageCache.get(path);
        // 判断是否存在软引用
        if (softBitmap == null) {
            return null;
        }
        // 取出Bitmap对象，如果由于内存不足Bitmap被回收，将取得空
        Bitmap bitmap = softBitmap.get();
        return bitmap;
    }

    public Bitmap loadBitmap(final String imageUrl, final ImageCallBack imageCallBack) {
        SoftReference<Bitmap> reference = imageCache.get(imageUrl);
        if (reference != null) {
            if (reference.get() != null) {
                return reference.get();
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(final Message msg) {
                // 加入到缓存中
                Bitmap bitmap = (Bitmap) msg.obj;
                imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
                if (imageCallBack != null) {
                    imageCallBack.getBitmap(bitmap);
                }
            }
        };
        new Thread() {
            public void run() {
                Message message = handler.obtainMessage();
                message.obj = downloadBitmap(imageUrl);
                handler.sendMessage(message);
            }
        }.start();
        return null;
    }

    public interface ImageCallBack {
        void getBitmap(Bitmap bitmap);
    }
    // ----其它工具----------------------------------------------------------------------------------

    /**
     * 从网上下载图片
     *
     * @param imageUrl
     * @return
     */
    private Bitmap downloadBitmap(String imageUrl) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * drawable 转bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * bitmap 转 drawable
     *
     * @param bm
     * @return
     */
    public static Drawable bitmap2Drable(Bitmap bm) {
        return new BitmapDrawable(bm);
    }

    /**
     * 把字节数组通过BASE64Encoder转换成字符串
     *
     * @param image
     * @return
     */
    public static String getBase64(byte[] image) {
        String string = "";
        try {
            BASE64Encoder encoder = new BASE64Encoder();
            string = encoder.encodeBuffer(image).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 把字节数据转换成Drawable
     *
     * @param imgByte 字节数据
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Drawable byte2Drawable(byte[] imgByte) {
        Bitmap bitmap;
        if (imgByte != null) {
            bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            Drawable drawable = new BitmapDrawable(bitmap);
            return drawable;
        }
        return null;
    }

    /**
     * 把图片转换成字节数组
     *
     * @return
     */
    public static byte[] bitmap2Byte(Bitmap bm) {
        Bitmap outBitmap = Bitmap.createScaledBitmap(bm, 150, bm.getHeight() * 150 / bm.getWidth(), true);
        if (bm != outBitmap) {
            bm.recycle();
            bm = null;
        }
        byte[] compressData = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            try {
                outBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            } catch (Exception e) {
                e.printStackTrace();
            }
            compressData = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressData;
    }

    /**
     * 缩放图片
     *
     * @param bitmap    原图片
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap setBitmapSize(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = (newWidth * 1.0f) / width;
        float scaleHeight = (newHeight * 1.0f) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 缩放图片
     *
     * @param bitmapPath 图片路径
     * @return
     */
    public static Bitmap setBitmapSize(String bitmapPath, float newWidth, float newHeight) {
        Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath);
        if (bitmap == null) {
            Logger.i("bitmap", "bitmap------------>发生未知异常！");
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = newWidth / width;
        float scaleHeight = newHeight / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 计算图片的缩放大小 如果==1，表示没变化，==2，表示宽高都缩小一倍 ----------------------------------------------------------------------------
     * inSampleSize是BitmapFactory.Options类的一个参数，该参数为int型， 他的值指示了在解析图片为Bitmap时在长宽两个方向上像素缩小的倍数。inSampleSize的默认值和最小值为1（当小于1时，解码器将该值当做1来处理），
     * 且在大于1时，该值只能为2的幂（当不为2的幂时，解码器会取与该值最接近的2的幂）。 例如，当inSampleSize为2时，一个2000*1000的图片，将被缩小为1000*500，相应地， 它的像素数和内存占用都被缩小为了原来的1/4：
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原始图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 根据计算出的inSampleSize生成Bitmap(此时的bitmap是经过缩放的图片)
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // 首先设置 inJustDecodeBounds=true 来获取图片尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * inJustDecodeBounds属性设置为true，decodeResource()方法就不会生成Bitmap对象，而仅仅是读取该图片的尺寸和类型信息：
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 计算 inSampleSize 的值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 根据计算出的 inSampleSize 来解码图片生成Bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 将图片保存到本地时进行压缩, 即将图片从Bitmap形式变为File形式时进行压缩,
     * 特点是: File形式的图片确实被压缩了, 但是当你重新读取压缩后的file为 Bitmap是,它占用的内存并没有改变
     *
     * @param bmp
     * @param file
     */
    public static void compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;// 个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片从本地读到内存时,进行压缩 ,即图片从File形式变为Bitmap形式
     * 特点: 通过设置采样率, 减少图片的像素, 达到对内存中的Bitmap进行压缩
     *
     * @param srcPath
     * @return
     */
    public static Bitmap compressImageFromFile(String srcPath, float pixWidth, float pixHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);
        options.inJustDecodeBounds = false;
        int w = options.outWidth;
        int h = options.outHeight;
        int scale = 1;
        if (w > h && w > pixWidth) {
            scale = (int) (options.outWidth / pixWidth);
        } else if (w < h && h > pixHeight) {
            scale = (int) (options.outHeight / pixHeight);
        }
        if (scale <= 0)
            scale = 1;
        options.inSampleSize = scale;// 设置采样率
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
        options.inPurgeable = true;// 同时设置才会有效
        options.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
        bitmap = BitmapFactory.decodeFile(srcPath, options);
        // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        // 其实是无效的,大家尽管尝试
        return bitmap;
    }

    /**
     * 判断照片的角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    /**
     * Android根据设备屏幕尺寸和dpi的不同，给系统分配的单应用程序内存大小也不同，具体如下表
     *
     * 屏幕尺寸 DPI 应用内存
     * small / normal / large ldpi / mdpi 16MB
     * small / normal / large tvdpi / hdpi 32MB
     * small / normal / large xhdpi 64MB
     * small / normal / large 400dpi 96MB
     * small / normal / large xxhdpi 128MB
     * -------------------------------------------------------
     * xlarge mdpi 32MB
     * xlarge tvdpi / hdpi 64MB
     * xlarge xhdpi 128MB
     * xlarge 400dpi 192MB
     * xlarge xxhdpi 256MB
     */

    /**
     * 图片转化成base64字符串
     *
     * @param imgPath
     * @return
     */
    public static String GetImageStr(String imgPath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = imgPath;// 待处理的图片
        InputStream in = null;
        byte[] data = null;
        String encode = null; // 返回Base64编码过的字节数组字符串
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            // 读取图片字节数组
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            encode = encoder.encode(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return encode;
    }

    /**
     * base64字符串转化成图片
     *
     * @param imgData     图片编码
     * @param imgFilePath 存放到本地路径
     * @return
     * @throws IOException
     */
    @SuppressWarnings("finally")
    public static boolean GenerateImage(String imgData, String imgFilePath, Context context) throws IOException { // 对字节数组字符串进行Base64解码并生成图片
        if (imgData == null) // 图像数据为空
            return false;
        imgData = imgData.replace("data:image/png;base64,", "");
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try {
            out = new FileOutputStream(imgFilePath);
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgData);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            out.write(b);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
            Intent intent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File file = new File(imgFilePath);
            Uri uri = Uri.fromFile(file);
            intent1.setData(uri);
            context.sendBroadcast(intent1);
            return true;
        }
    }



    public static Bitmap getViewBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED)
                ,View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
        view.layout(0,0,view.getMeasuredWidth(),view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public static void saveBitmap(Context context, Bitmap mBitmap,String fileName) {
        File filePic=null;
        String galleryPath= Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                +File.separator+"Desinfection_Share_img"+File.separator;
        try {
            filePic = new File(galleryPath + fileName+".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        MediaStore.Images.Media.insertImage(context.getContentResolver(),
                mBitmap, fileName, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(filePic);
        intent.setData(uri);
        context.sendBroadcast(intent);
        ToastUtil.showToast("保存图片成功");
        mBitmap.recycle();
        mBitmap=null;
    }


    /**
     * 获取当前View转换图片并保存本地
     * @param view
     * @return 返回图片本地路径
     */
    public static String viewSaveToImage(View view){
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setBackgroundColor(Color.TRANSPARENT);

        Bitmap cacheBmp=loadBitmapFromView(view);

        FileOutputStream fos;
        String imagePath="";
        try {
            boolean isHasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            if (isHasSDCard){
                File sdRoot = Environment.getExternalStorageDirectory();
                File file = new File(sdRoot, Calendar.getInstance().getTimeInMillis()+".png");
                fos = new FileOutputStream(file);
                imagePath = file.getAbsolutePath();
            }else
                throw new Exception("创建文件失败");

            cacheBmp.compress(Bitmap.CompressFormat.PNG,90,fos);

            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        view.destroyDrawingCache();
        return imagePath;
    }


    private static Bitmap loadBitmapFromView(View v){
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w,h,Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(Color.TRANSPARENT);

        v.layout(0,0,w,h);
        v.draw(canvas);

        return bmp;
    }




    public static View layoutView(View v,int width,int height){
        v.layout(0,0,width,height);
        int measureWidth = View.MeasureSpec.makeMeasureSpec(width,View.MeasureSpec.EXACTLY);
        int measureHeight= View.MeasureSpec.makeMeasureSpec(height,View.MeasureSpec.AT_MOST);
        v.measure(measureWidth,measureHeight);
        v.layout(0,0,v.getMeasuredWidth(),v.getMeasuredHeight());
        return v;
    }





}
