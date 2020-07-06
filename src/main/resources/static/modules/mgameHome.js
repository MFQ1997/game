;layui.define(["table", "form"], function (e) {
    var t = layui.$, i = layui.table;admin = layui.admin;
    layui.form;
    /*资讯管理*/
    i.render({
        elem: "#table-front-classify-tool",
        height: 450, //设置高度
        url: "/api/module/classify/list/",
        parseData: function(res){ //res 即为原始返回的数据
            return {
                "code":res.code,
                "msg":res.msg,
                "count":res.count,
                "data": res.data.list //解析数据列表
            };
            },
        cols: [
            [
                {type: "checkbox", fixed: "left"},
                {title: '序号',templet: '#xuhao'},
                {field: "id", title: "ID", sort: !0},
                {field: "img", width: 100, title: "图片", sort: !0,templet:"#imgTemplate"},
                {field: "title", width: 100, title: "标题", sort: !0},
                {field: "classifyId", title: "分类", sort: !0},
                {field: "time",title: "发布时间"},
                {field: "author",title: "发布者"},
                {field: "isTop",title: "是否置顶"},
                {field: "view",title: "浏览量"},
                {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-article-tool"}
                ]
            ],
        text: "对不起，加载出现异常！"
        }), i.on("tool(table-front-classify-tool)", function (e) {
        e.data;
        var id = e.data.id;
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {

            admin.req({
                url: "/api/article/deleteone/"+id,
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        e.del(), layer.close(t);
                        layer.msg(data.msg,{offset: '15px',icon:1,time: 1000},function () {
                            layer.close(index); //关闭弹层
                            table.reload('LAY-article-list');
                        })
                    }else {
                        layer.msg(data.msg,{icon:0,time:1000});
                        window.history.go(-1);
                    }
                }
            });
            return false;
        });
        /*编辑*/
        else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑",
                content: "/article/edit/"+id,
                data:[""],
                area: ["800px", "450px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e],
                        r = t.find("iframe").contents().find("#LAY-aticle-edit-submit");
                    l.layui.form.on("submit(LAY-aticle-edit-submit)", function (data) {
                        if(data.field.status == "on") {
                            data.field.status = "1";
                        } else {
                            data.field.status = "0";
                        }
                        if(data.field.isTop == "on") {
                            data.field.isTop = "1";
                        } else {
                            data.field.isTop = "0";
                        }
                        var field = data.field; //获取提交的字段
                        var article = JSON.stringify(field);
                        admin.req({
                            url:"/api/article",
                            type:"PUT",
                            dataType: "json",
                            contentType: "application/json",
                            data:article,
                            done: function(data){
                                if(data.code ==0){

                                    layer.msg(data.msg,{offset: '15px',icon:1,time: 1000},function () {
                                        layer.close(index); //关闭弹层
                                        layer.close(t);
                                        table.reload('LAY-article-list');
                                    })
                                }else {
                                    layer.msg(data.msg,{icon:0,time:1000});
                                    window.history.go(-1);
                                }
                            }
                        });

                    }), r.trigger("click")
                },success: function (e, t) {
                }
            })
        }
    })
        ,e("mgameHome", {})
});