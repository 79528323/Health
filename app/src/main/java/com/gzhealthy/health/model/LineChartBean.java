package com.gzhealthy.health.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/3/2
 */
//@lombok.Data
public class LineChartBean implements Serializable {

    /**
     * ERRORNO : 0
     * GRID0 : {"code":0,"message":"SUCCESS","result":{"compositeIndexGEM":[{"rate":"-0.00222918","tradeDate":"20180502"},{"rate":"0.01173907","tradeDate":"20180503"},{"rate":"0.00512795","tradeDate":"20180504"},{"rate":"0.02588620","tradeDate":"20180507"},{"rate":"0.02839673","tradeDate":"20180508"},{"rate":"0.02780967","tradeDate":"20180509"},{"rate":"0.03309878","tradeDate":"20180510"},{"rate":"0.01616309","tradeDate":"20180511"},{"rate":"0.01405908","tradeDate":"20180514"},{"rate":"0.02902977","tradeDate":"20180515"},{"rate":"0.02275095","tradeDate":"20180516"},{"rate":"0.01418314","tradeDate":"20180517"},{"rate":"0.01725636","tradeDate":"20180518"},{"rate":"0.03150485","tradeDate":"20180521"},{"rate":"0.03899213","tradeDate":"20180522"},{"rate":"0.02236271","tradeDate":"20180523"},{"rate":"0.01816963","tradeDate":"20180524"},{"rate":"-0.00057986","tradeDate":"20180525"}],"clientAccumulativeRate":[{"tradeDate":"20180502","value":0.03676598},{"tradeDate":"20180503","value":0.04389086},{"tradeDate":"20180504","value":0.02586678},{"tradeDate":"20180507","value":0.06254981},{"tradeDate":"20180508","value":0.08384138},{"tradeDate":"20180509","value":0.05759211},{"tradeDate":"20180510","value":0.06473651},{"tradeDate":"20180511","value":0.06764582},{"tradeDate":"20180514","value":0.08505964},{"tradeDate":"20180515","value":0.09219111},{"tradeDate":"20180516","value":0.06195802},{"tradeDate":"20180517","value":0.08178629},{"tradeDate":"20180518","value":0.06189841},{"tradeDate":"20180521","value":0.08498741},{"tradeDate":"20180522","value":0.14887381},{"tradeDate":"20180523","value":0.15332169},{"tradeDate":"20180524","value":0.18007851},{"tradeDate":"20180525","value":0.19270683}],"compositeIndexShanghai":[{"rate":"-0.00034196","tradeDate":"20180502"},{"rate":"0.00604335","tradeDate":"20180503"},{"rate":"0.00285572","tradeDate":"20180504"},{"rate":"0.01765377","tradeDate":"20180507"},{"rate":"0.02571709","tradeDate":"20180508"},{"rate":"0.02495562","tradeDate":"20180509"},{"rate":"0.02990723","tradeDate":"20180510"},{"rate":"0.02629005","tradeDate":"20180511"},{"rate":"0.02978395","tradeDate":"20180514"},{"rate":"0.03565177","tradeDate":"20180515"},{"rate":"0.02833467","tradeDate":"20180516"},{"rate":"0.02337625","tradeDate":"20180517"},{"rate":"0.03603623","tradeDate":"20180518"},{"rate":"0.04269927","tradeDate":"20180521"},{"rate":"0.04286441","tradeDate":"20180522"},{"rate":"0.02813968","tradeDate":"20180523"},{"rate":"0.02349564","tradeDate":"20180524"},{"rate":"0.01916534","tradeDate":"20180525"}],"compositeIndexShenzhen":[{"rate":"0.00178004","tradeDate":"20180502"},{"rate":"0.01299389","tradeDate":"20180503"},{"rate":"0.00985242","tradeDate":"20180504"},{"rate":"0.02925488","tradeDate":"20180507"},{"rate":"0.03712899","tradeDate":"20180508"},{"rate":"0.03531456","tradeDate":"20180509"},{"rate":"0.03925607","tradeDate":"20180510"},{"rate":"0.03000959","tradeDate":"20180511"},{"rate":"0.03360851","tradeDate":"20180514"},{"rate":"0.04102158","tradeDate":"20180515"},{"rate":"0.03650125","tradeDate":"20180516"},{"rate":"0.03012562","tradeDate":"20180517"},{"rate":"0.03371167","tradeDate":"20180518"},{"rate":"0.04270245","tradeDate":"20180521"},{"rate":"0.04274681","tradeDate":"20180522"},{"rate":"0.02970187","tradeDate":"20180523"},{"rate":"0.02321282","tradeDate":"20180524"},{"rate":"0.01198677","tradeDate":"20180525"}]}}
     */

    private int ERRORNO;
    private GRID0Bean GRID0;

    public int getERRORNO() {
        return ERRORNO;
    }

    public void setERRORNO(int ERRORNO) {
        this.ERRORNO = ERRORNO;
    }

    public GRID0Bean getGRID0() {
        return GRID0;
    }

    public void setGRID0(GRID0Bean GRID0) {
        this.GRID0 = GRID0;
    }

    public static class GRID0Bean implements Serializable {
        /**
         * code : 0
         * message : SUCCESS
         * result : {"compositeIndexGEM":[{"rate":"-0.00222918","tradeDate":"20180502"},{"rate":"0.01173907","tradeDate":"20180503"},{"rate":"0.00512795","tradeDate":"20180504"},{"rate":"0.02588620","tradeDate":"20180507"},{"rate":"0.02839673","tradeDate":"20180508"},{"rate":"0.02780967","tradeDate":"20180509"},{"rate":"0.03309878","tradeDate":"20180510"},{"rate":"0.01616309","tradeDate":"20180511"},{"rate":"0.01405908","tradeDate":"20180514"},{"rate":"0.02902977","tradeDate":"20180515"},{"rate":"0.02275095","tradeDate":"20180516"},{"rate":"0.01418314","tradeDate":"20180517"},{"rate":"0.01725636","tradeDate":"20180518"},{"rate":"0.03150485","tradeDate":"20180521"},{"rate":"0.03899213","tradeDate":"20180522"},{"rate":"0.02236271","tradeDate":"20180523"},{"rate":"0.01816963","tradeDate":"20180524"},{"rate":"-0.00057986","tradeDate":"20180525"}],"clientAccumulativeRate":[{"tradeDate":"20180502","value":0.03676598},{"tradeDate":"20180503","value":0.04389086},{"tradeDate":"20180504","value":0.02586678},{"tradeDate":"20180507","value":0.06254981},{"tradeDate":"20180508","value":0.08384138},{"tradeDate":"20180509","value":0.05759211},{"tradeDate":"20180510","value":0.06473651},{"tradeDate":"20180511","value":0.06764582},{"tradeDate":"20180514","value":0.08505964},{"tradeDate":"20180515","value":0.09219111},{"tradeDate":"20180516","value":0.06195802},{"tradeDate":"20180517","value":0.08178629},{"tradeDate":"20180518","value":0.06189841},{"tradeDate":"20180521","value":0.08498741},{"tradeDate":"20180522","value":0.14887381},{"tradeDate":"20180523","value":0.15332169},{"tradeDate":"20180524","value":0.18007851},{"tradeDate":"20180525","value":0.19270683}],"compositeIndexShanghai":[{"rate":"-0.00034196","tradeDate":"20180502"},{"rate":"0.00604335","tradeDate":"20180503"},{"rate":"0.00285572","tradeDate":"20180504"},{"rate":"0.01765377","tradeDate":"20180507"},{"rate":"0.02571709","tradeDate":"20180508"},{"rate":"0.02495562","tradeDate":"20180509"},{"rate":"0.02990723","tradeDate":"20180510"},{"rate":"0.02629005","tradeDate":"20180511"},{"rate":"0.02978395","tradeDate":"20180514"},{"rate":"0.03565177","tradeDate":"20180515"},{"rate":"0.02833467","tradeDate":"20180516"},{"rate":"0.02337625","tradeDate":"20180517"},{"rate":"0.03603623","tradeDate":"20180518"},{"rate":"0.04269927","tradeDate":"20180521"},{"rate":"0.04286441","tradeDate":"20180522"},{"rate":"0.02813968","tradeDate":"20180523"},{"rate":"0.02349564","tradeDate":"20180524"},{"rate":"0.01916534","tradeDate":"20180525"}],"compositeIndexShenzhen":[{"rate":"0.00178004","tradeDate":"20180502"},{"rate":"0.01299389","tradeDate":"20180503"},{"rate":"0.00985242","tradeDate":"20180504"},{"rate":"0.02925488","tradeDate":"20180507"},{"rate":"0.03712899","tradeDate":"20180508"},{"rate":"0.03531456","tradeDate":"20180509"},{"rate":"0.03925607","tradeDate":"20180510"},{"rate":"0.03000959","tradeDate":"20180511"},{"rate":"0.03360851","tradeDate":"20180514"},{"rate":"0.04102158","tradeDate":"20180515"},{"rate":"0.03650125","tradeDate":"20180516"},{"rate":"0.03012562","tradeDate":"20180517"},{"rate":"0.03371167","tradeDate":"20180518"},{"rate":"0.04270245","tradeDate":"20180521"},{"rate":"0.04274681","tradeDate":"20180522"},{"rate":"0.02970187","tradeDate":"20180523"},{"rate":"0.02321282","tradeDate":"20180524"},{"rate":"0.01198677","tradeDate":"20180525"}]}
         */

        private int code;
        private String message;
        private ResultBean result;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean implements Serializable {
            private List<CompositeIndexGEMBean> compositeIndexGEM;
            private List<ClientAccumulativeRateBean> clientAccumulativeRate;
            private List<CompositeIndexShanghaiBean> compositeIndexShanghai;
            private List<CompositeIndexShenzhenBean> compositeIndexShenzhen;

            public List<CompositeIndexGEMBean> getCompositeIndexGEM() {
                return compositeIndexGEM;
            }

            public void setCompositeIndexGEM(List<CompositeIndexGEMBean> compositeIndexGEM) {
                this.compositeIndexGEM = compositeIndexGEM;
            }

            public List<ClientAccumulativeRateBean> getClientAccumulativeRate() {
                return clientAccumulativeRate;
            }

            public void setClientAccumulativeRate(List<ClientAccumulativeRateBean> clientAccumulativeRate) {
                this.clientAccumulativeRate = clientAccumulativeRate;
            }

            public List<CompositeIndexShanghaiBean> getCompositeIndexShanghai() {
                return compositeIndexShanghai;
            }

            public void setCompositeIndexShanghai(List<CompositeIndexShanghaiBean> compositeIndexShanghai) {
                this.compositeIndexShanghai = compositeIndexShanghai;
            }

            public List<CompositeIndexShenzhenBean> getCompositeIndexShenzhen() {
                return compositeIndexShenzhen;
            }

            public void setCompositeIndexShenzhen(List<CompositeIndexShenzhenBean> compositeIndexShenzhen) {
                this.compositeIndexShenzhen = compositeIndexShenzhen;
            }

            public static class CompositeIndexGEMBean implements Serializable {
                /**
                 * rate : -0.00222918
                 * tradeDate : 20180502
                 */

                private String rate;
                private String tradeDate;

                public String getRate() {
                    return rate;
                }

                public void setRate(String rate) {
                    this.rate = rate;
                }

                public String getTradeDate() {
                    return tradeDate;
                }

                public void setTradeDate(String tradeDate) {
                    this.tradeDate = tradeDate;
                }
            }

            public static class ClientAccumulativeRateBean implements Serializable {
                /**
                 * tradeDate : 20180502
                 * value : 0.03676598
                 */

                private String tradeDate;
                private double value;

                public String getTradeDate() {
                    return tradeDate;
                }

                public void setTradeDate(String tradeDate) {
                    this.tradeDate = tradeDate;
                }

                public double getValue() {
                    return value;
                }

                public void setValue(double value) {
                    this.value = value;
                }
            }

            public static class CompositeIndexShanghaiBean implements Serializable {
                /**
                 * rate : -0.00034196
                 * tradeDate : 20180502
                 */

                private String rate;
                private String tradeDate;

                public String getRate() {
                    return rate;
                }

                public void setRate(String rate) {
                    this.rate = rate;
                }

                public String getTradeDate() {
                    return tradeDate;
                }

                public void setTradeDate(String tradeDate) {
                    this.tradeDate = tradeDate;
                }
            }

            public static class CompositeIndexShenzhenBean implements Serializable {
                /**
                 * rate : 0.00178004
                 * tradeDate : 20180502
                 */

                private String rate;
                private String tradeDate;

                public String getRate() {
                    return rate;
                }

                public void setRate(String rate) {
                    this.rate = rate;
                }

                public String getTradeDate() {
                    return tradeDate;
                }

                public void setTradeDate(String tradeDate) {
                    this.tradeDate = tradeDate;
                }
            }
        }
    }
}
