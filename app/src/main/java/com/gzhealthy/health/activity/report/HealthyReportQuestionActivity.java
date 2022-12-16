package com.gzhealthy.health.activity.report;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.HealthyReport;
import com.gzhealthy.health.model.QuePaper;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.ReportQueListDialog;
import com.gzhealthy.health.widget.ReportQuestionProgressView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 健康报告问卷
 */
public class HealthyReportQuestionActivity extends BaseAct implements View.OnClickListener {

    @BindView(R.id.report_select_question)
    LinearLayout linear_report_select_question;

    @BindView(R.id.report_generate)
    TextView tv_report_generate;

    @BindView(R.id.linear_generate)
    LinearLayout linear_generate;

    @BindView(R.id.option_linear)
    LinearLayout linear_option_linear;

    @BindView(R.id.que_content)
    TextView tv_que_content;

    @BindView(R.id.que_num)
    TextView tv_que_num;

    @BindView(R.id.previous)
    TextView tv_previous;

    @BindView(R.id.reportQuestionProgressView)
    ReportQuestionProgressView reportQuestionProgressView;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    Map<Integer, CheckBox> checkIds;

    private QuePaper paper;

    /**
     * 题目组下游标 (因题目可能会有缺失，不能直接用迭代index代 questionId
     */
    private int position = 0;

    private CheckBox checkBox;

    private ReportQueListDialog dialog;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_healthy_report_question;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        hideOrShowToolbar(true);
        hideOrShowAppbar(true);
        mImmersionBar.transparentStatusBar().statusBarDarkFont(false).init();
        BarUtils.addMarginTopEqualStatusBarHeight(ivBack);
        ivBack.setOnClickListener(v -> goBack());

        linear_report_select_question.setOnClickListener(this);
        tv_report_generate.setOnClickListener(this);
        tv_previous.setOnClickListener(this);
        checkIds = new HashMap<>();
        getPaper();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous:
                previousQue();
                break;

            case R.id.report_generate:
//                ToastUtil.showToast("报告待完成中");
//                HealthyReportResultActivity.inStance(this);
                submitPaper();
                break;

            case R.id.report_select_question:
                dialog = new ReportQueListDialog(this, this.paper.data, questionId -> {
                    initSetPosition(questionId);
                    initCompone(this.paper.data);
                    dialog.dismiss();
                });
                dialog.show();
                break;

            case R.id.iv_custom_toolbar_left:
                goBack();
                break;

            default:
                singleChecked(view.getId());
                break;
        }

    }


    /**
     * 获取问卷
     */
    public void getPaper() {
        Map<String, String> param = new HashMap<>();
        param.put(SPCache.KEY_TOKEN, SPCache.getString(SPCache.KEY_TOKEN, ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getQuestionPaper(param),
                new CallBack<QuePaper>() {

                    @Override
                    public void onResponse(QuePaper paper) {
                        if (paper.code == 1) {
                            //预存在当前页面临时缓存
                            HealthyReportQuestionActivity.this.paper = paper;
                            initSetPosition(paper.data.startQuestionId);
                            initCompone(paper.data);
                        }
                    }
                });
    }


    /**
     * 提交问卷
     */
    public void submitPaper() {
        if (tv_report_generate.isSelected()) {
            //生成体质报告
            Map<String, String> param = new HashMap<>();
            param.put(SPCache.KEY_TOKEN, SPCache.getString(SPCache.KEY_TOKEN, ""));
            HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().submitPaper(
                    this.paper.data.paperId, SPCache.getString(SPCache.KEY_TOKEN, "")),
                    new CallBack<BaseModel>() {

                        @Override
                        public void onResponse(BaseModel model) {
                            if (model.code == 1) {
                                HealthyReportLoadingActivity.inStance(HealthyReportQuestionActivity.this);
                                finish();
                            } else {
                                ToastUtil.showToast(model.msg);
                            }
                        }
                    });
        } else {
            //查看体质报告
            Map<String, String> param = new HashMap<>();
            String token = SPCache.getString(SPCache.KEY_TOKEN, "");
            HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getReportInfo(param, token),
                    new CallBack<HealthyReport>() {
                        @Override
                        public void onResponse(HealthyReport data) {
                            if (data.code == 1) {
                                HealthyReportResultActivity.inStance(HealthyReportQuestionActivity.this, data);
                            } else {
                                ToastUtil.showToast(data.msg);
                            }
                        }
                    });
        }
    }


    /**
     * 保存答案
     */
    public void saveAnswer(CheckBox box) {
        Map<String, String> param = new HashMap<>();
        param.put("paperId", String.valueOf(this.paper.data.paperId));
        int questionId = (int) box.getTag();
        param.put("questionId", String.valueOf(questionId));
        param.put("score", String.valueOf(box.getId()));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().saveAnswer(param, SPCache.getString(SPCache.KEY_TOKEN, "")),
                new CallBack<BaseModel<Boolean>>() {

                    @Override
                    public void onResponse(BaseModel<Boolean> model) {
                        if (model.code == 1) {
                            if (!tv_report_generate.isSelected()) {
                                tv_report_generate.setSelected(true);
                                tv_report_generate.setText("生成体质报告");
                            }
                            updateLocalData(questionId, box.getId());
                            //题目还未完成
                            if (!model.data) {
                                nextQue();
                            } else {
                                //返回true表示已经全部完成  显示生成报告按钮
                                linear_generate.setVisibility(View.VISIBLE);
                                //通知刷新报告首页获取按钮状态
                                RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_REPORT_BUTTON_STATUS);
                                HealthyReportQuestionActivity.this.paper.data.isDone = model.data;
                                if (position + 1 < HealthyReportQuestionActivity.this.paper.data.questionList.size()) {
                                    nextQue();
                                }
//                                ToastUtil.showToast(model.msg);
                            }
                        } else {
                            ToastUtil.showToast(model.msg);
                        }
                    }
                });
    }


//    /**
//     * 获取报告
//     * @param id
//     */
//    public void getReport(int id){
//        Map<String,String> param = new HashMap<>();
//        if (id > 0){
//            param.put("id",String.valueOf(id));//报告id，不传则返回最新的报告
//        }
////        param.put("type","0");//类型，0-自动生成，1-主动生成，默认为：0
//        String token = SPCache.getString(SPCache.KEY_TOKEN,"");
//        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getReportInfo(param,token),
//                new CallBack<HealthyReport>() {
//                    @Override
//                    public void onResponse(HealthyReport data) {
//                        if (data.code == 1){
//                            HealthyReportResultActivity.inStance(HealthyReportQuestionActivity.this,data);
//                            finish();
//                        }else {
//                            ToastUtil.showToast(data.msg);
//                        }
//                    }
//                });
//    }


    /**
     * 下一题
     */
    public void nextQue() {
        position += 1;
        if (!tv_previous.isShown()) {
            tv_previous.setVisibility(View.VISIBLE);
        }
        initCompone(this.paper.data);
    }


    /**
     * 上一题
     */
    public void previousQue() {
        position -= 1;
        if (position < 0) {
            position = 0;
        }
        tv_previous.setVisibility(position <= 1 ? View.INVISIBLE : View.VISIBLE);
        linear_generate.setVisibility(position == this.paper.data.questionList.size() ? View.VISIBLE : View.GONE);
        initCompone(this.paper.data);
    }


    public void initCompone(QuePaper.DataBean paper) {
        if (paper.questionList.isEmpty())
            return;

        if (checkIds != null && checkIds.size() > 0) {
            checkIds.clear();
            linear_option_linear.removeAllViews();
        }

        //判断第一题时隐藏 按钮
        tv_previous.setVisibility(position < 1 ? View.INVISIBLE : View.VISIBLE);
        //判断是否已经全部完成 则显示生成按钮
        linear_generate.setVisibility(paper.isDone ? View.VISIBLE : View.GONE);

        for (int index = 0; index < paper.questionList.size(); index++) {
            if (index == position) {
                QuePaper.DataBean.QuestionListBean listBean = paper.questionList.get(index);
                reportQuestionProgressView.setvMax(this.paper.data.questionList.size());
                reportQuestionProgressView.setCurrent(index + 1);
                resetQuePos(listBean);
                int queNum = index + 1;
                tv_que_num.setText(String.valueOf(((queNum < 10) ? ("0" + queNum) : queNum)));
                tv_que_content.setText(listBean.question);
            }
        }
    }


    /**
     * 创建答题卡选项
     *
     * @param bean
     */
    public void resetQuePos(QuePaper.DataBean.QuestionListBean bean) {
//        tv_position.setText(bean.questionId + "/" + this.paper.data.questionList.size());
        for (int index = 0; index < bean.options.size(); index++) {
            QuePaper.DataBean.QuestionListBean.OptionsBean optionsBean = bean.options.get(index);
            answerOptionCheck(optionsBean, bean.selected, bean.questionId);
        }
    }


    /**
     * 答题卡
     *
     * @param bean
     * @param selected   返回当前选择ID
     * @param questionId 题目ID
     */
    public void answerOptionCheck(QuePaper.DataBean.QuestionListBean.OptionsBean bean, int selected, int questionId) {
        CheckBox check = new CheckBox(this);
        check.setId(Integer.valueOf(bean.key));
        check.setChecked(Integer.valueOf(bean.key) == selected);
        check.setText(bean.value);
        check.setBackgroundResource(R.drawable.selector_answer);
        check.setGravity(Gravity.CENTER);
//        check.setTextColor(ContextCompat.getColor(this,R.color.global_333333));
        check.setTextColor(Integer.valueOf(bean.key) == selected ?
                Color.parseColor("#4087fb") : ContextCompat.getColor(this, R.color.global_333333));
        check.setTextSize(14);
        check.setElevation(5f);
        check.setTag(questionId);
        check.setButtonDrawable(null);
        check.setOnClickListener(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 20;
        lp.leftMargin = lp.rightMargin = lp.bottomMargin = 5;
        linear_option_linear.addView(check, lp);
        checkIds.put(Integer.valueOf(bean.key), check);
    }


    /**
     * 单选模式
     *
     * @param id
     */
    public void singleChecked(int id) {
        for (int index = 1; index < checkIds.size() + 1; index++) {
            CheckBox box = checkIds.get(index);
            boolean isCheck = id == box.getId();
            box.setChecked(isCheck);
            box.setTextColor(box.isChecked() ?
                    Color.parseColor("#4087fb") : ContextCompat.getColor(this, R.color.global_333333));
        }
        for (int index = 1; index < checkIds.size() + 1; index++) {
            CheckBox box = checkIds.get(index);
            if (box.isChecked()) {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.healthy_report_question);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new Handler().postDelayed(() -> saveAnswer(box), 300);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                box.startAnimation(animation);
                break;
            }
        }
    }


    /**
     * 更新本地数据
     *
     * @param selected
     */
    public void updateLocalData(int questionId, int selected) {
        if (this.paper.data != null && this.paper.data.questionList.size() > 0) {
            Iterator<QuePaper.DataBean.QuestionListBean> listBeanIterator = paper.data.questionList.listIterator();
            while (listBeanIterator.hasNext()) {
                QuePaper.DataBean.QuestionListBean bean = listBeanIterator.next();
                //因为数据只获取一次在本地缓存，
                if (bean.questionId == questionId) {
                    bean.selected = selected;
                    return;
                }
            }
        }
    }

    /**
     * 重置题目下游标
     */
    public void initSetPosition(int questionId) {
        if (this.paper == null)
            return;

        List<QuePaper.DataBean.QuestionListBean> questionListBeanList = this.paper.data.questionList;
        for (int i = 0; i < questionListBeanList.size(); i++) {
            QuePaper.DataBean.QuestionListBean bean = questionListBeanList.get(i);
            if (bean.questionId == questionId) {
                this.position = i;//需要重新判断来重围取题下游标
                return;
            }
        }
    }

    public static void inStance(Context context) {
        context.startActivity(new Intent(context, HealthyReportQuestionActivity.class));
    }
}
