package com.ishow.ischool.bean.saleprocess;

import java.io.Serializable;

/**
 * Created by MrS on 2016/9/13.
 */
public class SaleProcess implements Serializable {


    /**
     * chart : {"date":["2016-09-06","2016-09-07","2016-09-08","2016-09-09","2016-09-10","2016-09-11","2016-09-12"],"full_amount":["10","20","33","30","35","29","20"],"apply_number":["10","20","33","30","35","29","20"]}
     * tablehead : ["晨读讲师","晨读开班人数","邀约人数","邀约率","公开课现场人数","邀约到课率","报名人数","公开课报名人数","全款人数","全款率"]
     * tablebody : [["吴家俊","100","120","30","13","0.5","50","30","20","0.3"],["吴家俊","100","120","30","13","0.5","50","30","20","0.3"],["吴家俊","100","120","30","13","0.5","50","30","20","0.3"],["吴家俊","100","120","30","13","0.5","50","30","20","0.3"],["吴家俊","100","120","30","13","0.5","50","30","20","0.3"]]
     */

    public ChartBean chart;
    public Table table;
    public Principal principal;
    /*public SaleTable1 table1;
    public SaleTabel2 table2;*/

}
