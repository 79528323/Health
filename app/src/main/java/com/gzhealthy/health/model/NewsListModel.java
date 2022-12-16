package com.gzhealthy.health.model;

import com.gzhealthy.health.model.base.Base1Model;

import java.io.Serializable;
import java.util.List;


public class NewsListModel extends Base1Model {


    /**
     * msg : 资讯列表获取成功
     * code : 1
     * data : [{"addTime":"2020-12-08 15:05","author":"体安","cid":"1","content":"<p style=\"-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal\"><span style=\"-webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-family: 宋体; font-size: 12px;\">自从有了冰箱之后，很多人习惯一次性买好一周的菜，然而有时候不小心就会发现食物发霉了。<\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal\"><span style=\"font-family:宋体\"><br/><\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal\"><span style=\"font-family:宋体\">如果不是烂得很多，就直接削坏的部分继续食用。虽然这样可以极大程度减少损失，但是这样做是否健康呢？食物一发霉，是要全部扔掉，还是只去掉坏的部分就可以了呢？<\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal\"><span style=\"font-family:宋体\"><br/><\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal\"><span style=\"font-family: 宋体; -webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-weight: bold; color: rgb(70, 172, 200); font-size: 16px;\"><span style=\"-webkit-tap-highlight-color: transparent; font-family: 宋体; font-size: x-large; font-weight: bold; color: rgb(70, 172, 200);\">煮<\/span><span style=\"-webkit-tap-highlight-color: transparent; font-family: 宋体; font-size: x-large; font-weight: bold; color: rgb(70, 172, 200);\">熟是不是可以杀死霉菌？<\/span><\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal\"><span style=\"font-family: 宋体; -webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-weight: bold; color: rgb(70, 172, 200); font-size: 16px;\"><br/><\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal\"><span style=\"font-family: 宋体; font-size: 12px;\">高温加热是许多人想到的直接杀菌方式。事实上，这种方法并不绝对，对于一些细菌确实有效，但是对于常见的黄曲霉毒素、展青霉素就不一定有效。<\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;\"><span style=\"-webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-family: 宋体; font-size: 12px;\"><br/><\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;\"><span style=\"-webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-family: 宋体; font-size: 12px;\">黄曲霉毒素存在于粮食、坚果、油类等食物中，常规的100℃很难把它杀死，只有当温度达到280℃，才能将细菌分解。<\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;\"><span style=\"font-size: 12px;\"><span style=\"font-size: 12px; -webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-family: 宋体;\"><br/><\/span><span style=\"font-size: 12px; font-family: 宋体;\">展青霉素存在于苹果、桃、梨、香蕉等水果中，即使高温可以降低毒素含量，但仍然无法完全消除。<\/span><\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;\"><span style=\"font-family: 宋体; font-size: 12px;\"><br/><\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;\"><span style=\"font-family: 宋体; font-size: 12px;\">当发现食物发霉，最简单也是最正确的做法\u2014\u2014直接扔掉，不要吃坏肚子。<\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;\"><span style=\"-webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-family: 宋体; font-size: 12px;\"><br/><\/span><span style=\"color: rgb(70, 172, 200); font-family: 宋体; font-size: x-large; font-weight: 700;\">常见的发霉食物有哪些？<\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;\"><span style=\"font-family: 宋体; font-size: 16px;\"><br/><\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;\"><span style=\"color: rgb(249, 150, 59); font-family: 宋体; font-size: large; font-weight: 700;\">坚果<\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal\"><span style=\"-webkit-tap-highlight-color: transparent;box-sizing: border-box;padding: 0px;font-family: 宋体;font-size: large\"><br/><\/span><span style=\"font-family:宋体\">坚果油性多，保存不当容易受潮发霉，产生黄曲霉毒素。黄曲霉毒素作为一类天然致癌物，对肝脏伤害极大，长期食用会增加肝癌等疾病发生率。<\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal\"><span style=\"-webkit-tap-highlight-color: transparent;box-sizing: border-box;padding: 0px;font-family: 宋体;font-size: large\"><br/><\/span><span style=\"font-family:宋体\">大米是通过稻谷加工失去颖壳和种皮后的白米。胚乳直接与空气接触，对温、湿度影响比较敏感，吸湿性强，周围的霉菌容易直接危害污染。<\/span><\/p><p style=\"-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; font-size: 12px; white-space: normal;\"><br/><\/p>","id":1,"imageInput":"1607570204203741172.png","shareSynopsis":"食物烂了一点，还能不能吃？切记：这几类真不能吃！","shareTitle":"食物烂了一点，还能不能吃？切记：这几类真不能吃！","sort":1,"status":true,"synopsis":"食物烂了一点，还能不能吃？切记：这几类真不能吃！","title":"食物烂了一点，还能不能吃？切记：这几类真不能吃！","visit":"91"}]
     */

    private String msg;
    private int code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * addTime : 2020-12-08 15:05
         * author : 体安
         * cid : 1
         * content : <p style="-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal"><span style="-webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-family: 宋体; font-size: 12px;">自从有了冰箱之后，很多人习惯一次性买好一周的菜，然而有时候不小心就会发现食物发霉了。</span></p><p style="-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal"><span style="font-family:宋体"><br/></span></p><p style="-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal"><span style="font-family:宋体">如果不是烂得很多，就直接削坏的部分继续食用。虽然这样可以极大程度减少损失，但是这样做是否健康呢？食物一发霉，是要全部扔掉，还是只去掉坏的部分就可以了呢？</span></p><p style="-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal"><span style="font-family:宋体"><br/></span></p><p style="-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal"><span style="font-family: 宋体; -webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-weight: bold; color: rgb(70, 172, 200); font-size: 16px;"><span style="-webkit-tap-highlight-color: transparent; font-family: 宋体; font-size: x-large; font-weight: bold; color: rgb(70, 172, 200);">煮</span><span style="-webkit-tap-highlight-color: transparent; font-family: 宋体; font-size: x-large; font-weight: bold; color: rgb(70, 172, 200);">熟是不是可以杀死霉菌？</span></span></p><p style="-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal"><span style="font-family: 宋体; -webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-weight: bold; color: rgb(70, 172, 200); font-size: 16px;"><br/></span></p><p style="-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal"><span style="font-family: 宋体; font-size: 12px;">高温加热是许多人想到的直接杀菌方式。事实上，这种方法并不绝对，对于一些细菌确实有效，但是对于常见的黄曲霉毒素、展青霉素就不一定有效。</span></p><p style="-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;"><span style="-webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-family: 宋体; font-size: 12px;"><br/></span></p><p style="-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;"><span style="-webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-family: 宋体; font-size: 12px;">黄曲霉毒素存在于粮食、坚果、油类等食物中，常规的100℃很难把它杀死，只有当温度达到280℃，才能将细菌分解。</span></p><p style="-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;"><span style="font-size: 12px;"><span style="font-size: 12px; -webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-family: 宋体;"><br/></span><span style="font-size: 12px; font-family: 宋体;">展青霉素存在于苹果、桃、梨、香蕉等水果中，即使高温可以降低毒素含量，但仍然无法完全消除。</span></span></p><p style="-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;"><span style="font-family: 宋体; font-size: 12px;"><br/></span></p><p style="-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;"><span style="font-family: 宋体; font-size: 12px;">当发现食物发霉，最简单也是最正确的做法——直接扔掉，不要吃坏肚子。</span></p><p style="-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;"><span style="-webkit-tap-highlight-color: transparent; box-sizing: border-box; padding: 0px; font-family: 宋体; font-size: 12px;"><br/></span><span style="color: rgb(70, 172, 200); font-family: 宋体; font-size: x-large; font-weight: 700;">常见的发霉食物有哪些？</span></p><p style="-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;"><span style="font-family: 宋体; font-size: 16px;"><br/></span></p><p style="-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; white-space: normal;"><span style="color: rgb(249, 150, 59); font-family: 宋体; font-size: large; font-weight: 700;">坚果</span></p><p style="-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal"><span style="-webkit-tap-highlight-color: transparent;box-sizing: border-box;padding: 0px;font-family: 宋体;font-size: large"><br/></span><span style="font-family:宋体">坚果油性多，保存不当容易受潮发霉，产生黄曲霉毒素。黄曲霉毒素作为一类天然致癌物，对肝脏伤害极大，长期食用会增加肝癌等疾病发生率。</span></p><p style="-webkit-tap-highlight-color: transparent;box-sizing: border-box;margin-top: 0px;margin-bottom: 0px;padding: 0px;font-family: 微软雅黑;max-width: 100%;font-size: 12px;white-space: normal"><span style="-webkit-tap-highlight-color: transparent;box-sizing: border-box;padding: 0px;font-family: 宋体;font-size: large"><br/></span><span style="font-family:宋体">大米是通过稻谷加工失去颖壳和种皮后的白米。胚乳直接与空气接触，对温、湿度影响比较敏感，吸湿性强，周围的霉菌容易直接危害污染。</span></p><p style="-webkit-tap-highlight-color: transparent; box-sizing: border-box; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; max-width: 100%; font-size: 12px; white-space: normal;"><br/></p>
         * id : 1
         * imageInput : 1607570204203741172.png
         * shareSynopsis : 食物烂了一点，还能不能吃？切记：这几类真不能吃！
         * shareTitle : 食物烂了一点，还能不能吃？切记：这几类真不能吃！
         * sort : 1
         * status : true
         * synopsis : 食物烂了一点，还能不能吃？切记：这几类真不能吃！
         * title : 食物烂了一点，还能不能吃？切记：这几类真不能吃！
         * visit : 91
         */

        private String addTime;
        private String author;
        private String cid;
        private String content;
        private int id;
        private String imageInput;
        private String shareSynopsis;
        private String shareTitle;
        private int sort;
        private boolean status;
        private String synopsis;
        private String title;
        private String visit;
        private int isCollection;

        public int getIsCollection() {
            return isCollection;
        }

        public void setIsCollection(int isCollection) {
            this.isCollection = isCollection;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImageInput() {
            return imageInput;
        }

        public void setImageInput(String imageInput) {
            this.imageInput = imageInput;
        }

        public String getShareSynopsis() {
            return shareSynopsis;
        }

        public void setShareSynopsis(String shareSynopsis) {
            this.shareSynopsis = shareSynopsis;
        }

        public String getShareTitle() {
            return shareTitle;
        }

        public void setShareTitle(String shareTitle) {
            this.shareTitle = shareTitle;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getSynopsis() {
            return synopsis;
        }

        public void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVisit() {
            return visit;
        }

        public void setVisit(String visit) {
            this.visit = visit;
        }
    }

}
