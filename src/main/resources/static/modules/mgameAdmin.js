;layui.define(["table", "form"], function (e) {
    var t = layui.$, i = layui.table;admin = layui.admin;
    layui.form;
    /*前台用户模块*/
    i.render({
        elem: "#LAY-customer-list",
        url: "/api/customer",
        request: {
            pageName: 'page' //页码的参数名称，默认：page
            ,limitName: 'row' //每页数据量的参数名，默认：limit
            },
        parseData: function(res){ //res 即为原始返回的数据
        return {
            "code":res.code,
            "msg":res.msg,
            "count":res.coun,
            "data": res.data.list //解析数据列表
        };
    },
        cols: [
            [
                {type: "checkbox", fixed: "left"},
                {title: '序号',templet: '#xuhao'},
                {field: "customerId", width: 100, title: "ID", sort: !0},
                {field: "userName",title: "用户名",minWidth: 100},
                {field: "trueName",title: "真实名字"},
                {field: "introduce",title: "介绍"},
                {field: "loginTime",title: "登录时间"},
                {field: "loginIp",title: "登录ip"},
                {field: "email", title: "邮箱"},
                {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-customer-list"}
            ]
        ],
        page: true,
        limit: 10,
        height: 450, //设置高度
        text: {
            none: '暂无相关数据', //默认：无数据。注：该属性为 layui 2.2.5 开始新增
            error: '对不起，加载异常'
        }
    })
    , i.on("tool(LAY-customer-list)", function (e) {
        e.data;
        var id = e.data.id;
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {

            admin.req({
                url: "/api/customer",
                data:{"ids":id},
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        console.log(data.message);
                        e.del(), layer.close(t);
                        layer.msg(data.message,{offset: '15px',icon:1,time: 1000},function () {
                            layer.close(index); //关闭弹层
                            table.reload('LAY-customer-list');
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
                anim: 1,
                type: 2,
                title: "编辑",
                content: "/customer/edit?id="+id,
                data:[""],
                area: ["1000px", "480px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e],
                        r = t.find("iframe").contents().find("#LAY-user-role-submit");
                    l.layui.form.on("submit(LAY-user-role-submit)", function (t) {
                        t.field;
                        i.reload("LAY-customer-list"), layer.close(e)
                    }), r.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }),
        /*帖子管理*/
    i.render({
        elem: "#LAY-topic-list",
        url: "/api/topic",
        request: {
            pageName: 'page' //页码的参数名称，默认：page
            ,limitName: 'row' //每页数据量的参数名，默认：limit
        },
        parseData: function(res){ //res 即为原始返回的数据
            return {
                "code":0,
                "msg":res.msg,
                "count":res.count,
                "data": res.data.list //解析数据列表
            };
        },
        cols: [
            [
                {type: "checkbox", fixed: "left"},
                {title: '序号',templet: '#xuhao',width:40},
                {field: "classifyId", width: 100, title: "分类", sort: !0},
                {field: "title",title: "标题",width: 200},
                {field: "createTime",title: "发布时间"},
                {field: "userId",title: "发布者"},
                {field: "isTop",title: "是否置顶",templet:"#isTopTpl"},
                {field: "isGood",title: "是否加精",templet:"#isGoodTpl"},
                {field: "view",title: "浏览量",width:60},
                {field: "collet", title: "收藏量",width:60},
                {title: "操作",width: 100,align: "center",fixed: "right", toolbar: "#table-topic-tools"}
            ]
        ],
        page: true,
        limit: 10,
        height: 450, //设置高度
        text: {
            none: '暂无相关数据', //默认：无数据。注：该属性为 layui 2.2.5 开始新增
            error: '对不起，加载异常'
        }
    }), i.on("tool(LAY-topic-list)", function (e) {
        e.data;
        var id = e.data.id;
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {

            admin.req({
                url: "/api/customer",
                data:{"ids":id},
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        console.log(data.message);
                        e.del(), layer.close(t);
                        layer.msg(data.msg,{offset: '15px',icon:1,time: 1000},function () {
                            layer.close(index); //关闭弹层
                            table.reload('LAY-topic-list');
                        })
                    }else {
                        layer.msg(data.msg,{icon:0,time:1000});
                        window.history.go(-1);
                    }
                }
            });
            return false;

        });
    }), /*结束帖子管理*/

    /*操作日志*/
        i.render({
            elem: "#LAY-log-list",
            url: "/api/log",
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'row' //每页数据量的参数名，默认：limit
            },
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
                    {field: "id", width: 100, title: "ID", sort: !0},
                    {field: "userName",title: "用户名",minWidth: 100},
                    {field: "operation",title: "操作"},
                    {field: "method",title: "操作方法"},
                    {field: "ip",title: "操作ip"},
                    {field: "createTime",title: "操作时间"},
                    {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-log-list"}
                ]
            ],
            page: true,
            limit: 10,
            height: 450, //设置高度
            text: {
                none: '暂无相关数据', //默认：无数据。注：该属性为 layui 2.2.5 开始新增
                error: '对不起，加载异常'
            }
        }), i.on("tool(LAY-customer-list)", function (e) {
        e.data;
        var id = e.data.id;
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {

            admin.req({
                url: "/api/customer",
                data:{"ids":id},
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        console.log(data.message);
                        e.del(), layer.close(t);
                        layer.msg(data.message,{offset: '15px',icon:1,time: 1000},function () {
                            layer.close(index); //关闭弹层
                            table.reload('LAY-customer-list');
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
                anim: 1,
                type: 2,
                title: "编辑",
                content: "/customer/edit?id="+id,
                data:[""],
                area: ["1000px", "480px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e],
                        r = t.find("iframe").contents().find("#LAY-user-role-submit");
                    l.layui.form.on("submit(LAY-user-role-submit)", function (t) {
                        t.field;
                        i.reload("LAY-customer-list"), layer.close(e)
                    }), r.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }),
    /*结束操作日志*/

        /*板块管理*/
        i.render({
            elem: "#LAY-forum_module-list",
            url: "/api/module",
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'row' //每页数据量的参数名，默认：limit
            },
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
                    {title: '序号',width:60,templet: '#xuhao'},
                    {field: "name",title: "板块名",minWidth: 100},
                    {field: "photo",title: "图标", align: "center",templet:"#imgTemplate"},
                    {field: "masterName",title: "版主"},
                    {field: "status",title: "状态",templet:"#statusTpl"},
                    {field: "createTime",title: "操作时间"},
                    {field: "remark",title: "板块简介"},
                    {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-forum_module-right-tools"}
                ]
            ],
            page: true,
            limit: 10,
            height: 450, //设置高度
            text: {
                none: '暂无相关数据', //默认：无数据。注：该属性为 layui 2.2.5 开始新增
                error: '对不起，加载异常'
            }
        }), i.on("tool(LAY-forum_module-list)", function (e) {
        e.data;var id = e.data.id;
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {
            admin.req({
                url: "/api/module/deleteone/"+id,
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        console.log(data.msg);
                        e.del(), layer.close(t);
                        layer.msg(data.msg,{offset: '15px',icon:1,time: 1000},function () {
                            layer.close(index); //关闭弹层
                            i.reload('LAY-forum_module-list');
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
                anim: 1,
                type: 2,
                title: "编辑",
                content: "/forum/module/edit?id="+id,
                data:[""],
                area: ["800px", "400px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e],
                        r = t.find("iframe").contents().find("#LAY-module-front-submit");
                    l.layui.form.on("submit(LAY-module-front-submit)", function (data) {

                        if(data.field.status == "on") {data.field.status = "1";}
                        else {data.field.status = "0";}
                        var field = data.field; //获取提交的字段
                        var module = JSON.stringify(field);
                        admin.req({
                            url:"/api/module/",
                            type:"PUT",
                            dataType: "json",
                            contentType: "application/json",
                            data:module
                            ,done: function(data){
                                if(data.code ==0){
                                    layer.msg(data.msg,{icon:1,time: 1000},function () {
                                        layer.close(index); //关闭弹层
                                        i.reload('LAY-forum_module-list');
                                    })
                                }else {
                                    layer.msg(data.msg,{icon:0,time:1000});
                                    window.history.go(-1);
                                }
                            }
                        });
                        i.reload("LAY-forum_module-list"), layer.close(e)
                    }), r.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }),
        /*结束板块*/
    /*资讯管理*/
    i.render({
        elem: "#LAY-article-list",
        height: 450, //设置高度
        url: "/api/article",
        request: {
            pageName: 'page' //页码的参数名称，默认：page
            ,limitName: 'row' //每页数据量的参数名，默认：limit
        },
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
                {title: '序号',templet: '#xuhao',width:60},
                {field: "img", width: 100, title: "图片", sort: !0,templet:"#imgTemplate"},
                {field: "title", width: 250, title: "标题", sort: !0},
                {field: "time",title: "发布时间"},
                {field: "view",title: "浏览量",width:80},
                {field: "status", width: 80,title: "状态", templet: function (d) {
                        if (d.status == 0) {
                            return '<span class="layui-badge layui-bg-gray">未审核</span>';
                        }
                        if (d.status == 1) {
                            return '<span class="layui-badge layui-bg-red">未通过</span>';
                        } else {
                            return '<span class="layui-badge layui-bg-green">已发布</span>';
                        }
                    }},
                {title: "操作",width: 250,align: "center",fixed: "right", toolbar: "#table-article-tool"}
            ]
        ],
        page: true,
        limit: 10,
        /*height: "full-220",*/
        text: {
            none: '暂无相关数据', //默认：无数据。注：该属性为 layui 2.2.5 开始新增
            error: '对不起，加载异常'
        }
    }),
        i.on("tool(LAY-article-list)", function (e) {
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
                anim: 1,
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

                                        layer.msg(data.msg,{icon:1,time: 1000},function () {
                                            /*layer.close(index);
                                            layer.close(t);*/
                                            window.location.reload();
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
        /*审核*/
        else if ("check" === e.event) {
            t(e.tr);
            layer.open({
                anim: 1,
                type: 2,
                title: "审核",
                content: "/article/check/"+id,
                data:[""],
                area: ["100%", "100%"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    //var l = window["layui-layer-iframe" + e];
                    var index = parent.layer.getFrameIndex(window.name);
                    layer.msg("审核结束",{icon:1,time:1000},function () {
                        layer.close(index); //关闭弹层
                        window.location.reload();
                    });
                },success: function (e, t) {}
            })
        }
    }),
        /*安装包管理*/
        i.render({
            elem: "#LAY-apk-list",
            url: "/api/apk",
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'row' //每页数据量的参数名，默认：limit
            },
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
                    {title: '序号',width:60,templet: '#xuhao'},
                    {field: "img",title: "图标", align: "center",templet:"#imgTemplate"},
                    {field: "name",title: "程序名",minWidth: 100},
                    {field: "url",title: "路径",minWidth: 100},
                    {field: "size",title: "大小"},
                    {field: "status",title: "状态",templet:"#statusTpl"},
                    {field: "time",title: "时间"},
                    {field: "num",title: "下载量"},
                    {field: "remark",title: "板块简介"},
                ]
            ],
            page: true,
            limit: 10,
            height: 450, //设置高度
            text: {
                none: '暂无相关数据', //默认：无数据。注：该属性为 layui 2.2.5 开始新增
                error: '对不起，加载异常'
            }
        }),

        /*视频管理*/
        i.render({
            elem: "#LAY-video-list",
            url: "/api/video",
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'row' //每页数据量的参数名，默认：limit
            },
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
                    {title: '序号',width:60,templet: '#xuhao'},
                    {field: "title",title: "标题",minWidth: 100},
                    {field: "url",title: "路径",minWidth: 100},
                    {field: "author",title: "作者"},
                    {field: "status",title: "状态",templet:"#statusTpl"},
                    {field: "time",title: "时间"},
                ]
            ],
            page: true,
            limit: 10,
            height: 450, //设置高度
            text: {
                none: '暂无相关数据', //默认：无数据。注：该属性为 layui 2.2.5 开始新增
                error: '对不起，加载异常'
            }
        }),
        /*评论管理*/
        i.render({
            elem: "#LAY-comment-list",
            url: "/api/comment",
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'row' //每页数据量的参数名，默认：limit
            },
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
                    {title: '序号',width:60,templet: '#xuhao'},
                    {field: "topicName",title: "被评论的帖子(话题)"},
                    {field: "userName",title: "评论者", align: "center"},
                    {field: "content",title: "评论内容"},
                    {field: "time",title: "评论时间"},
                    {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-comment-tools"}
                ]
            ],
            page: true,
            limit: 10,
            height: 450, //设置高度
            text: {
                none: '暂无相关数据', //默认：无数据。注：该属性为 layui 2.2.5 开始新增
                error: '对不起，加载异常'
            }
        }), i.on("tool(LAY-comment-list)", function (e) {
        e.data;
        var id = e.data.id;
        console.log(id);
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {
            admin.req({
                url: "/api/comment/deleteone/"+id,
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        console.log(data.msg);
                        e.del(), layer.close(t);
                        layer.msg(data.msg,{offset: '15px',icon:1,time: 1000},function () {
                            layer.close(index); //关闭弹层
                            i.reload('LAY-comment-list');
                        })
                    }else {
                        layer.msg(data.msg,{icon:0,time:1000});
                        window.history.go(-1);
                    }
                }
            });
            return false;

        });
    }),/*结束评论管理*/

        /*评论回复管理*/
        i.render({
            elem: "#LAY-reply-list",
            url: "/api/reply",
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'row' //每页数据量的参数名，默认：limit
            },
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
                    {title: '序号',width:60,templet: '#xuhao'},
                    {field: 'topicName',title: '帖子'},
                    {field: "commentContent",title: "回复的评论"},
                    {field: "replyId",title: "回复目标的编号", align: "center"},
                    {field: "content",title: "回复内容"},
                    {field: "userName",title: "回复者"},
                    {field: "toUserName",title: "被回复者"},
                    {field: "time",title: "回复评论时间"},
                    {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-reply-tools"}
                ]
            ],
            page: true,
            limit: 10,
            height: 450, //设置高度
            text: {
                none: '暂无相关数据', //默认：无数据。注：该属性为 layui 2.2.5 开始新增
                error: '对不起，加载异常'
            }
            /*text: "对不起，加载出现异常！"*/
        }), i.on("tool(LAY-reply-list)", function (e) {
        e.data;
        var id = e.data.id;
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {
            admin.req({
                url: "/api/reply/deleteone/"+id,
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        console.log(data.msg);
                        e.del(), layer.close(t);
                        layer.msg(data.msg,{offset: '15px',icon:1,time: 1000},function () {
                            layer.close(index); //关闭弹层
                            i.reload('LAY-reply-list');
                        })
                    }else {
                        layer.msg(data.msg,{icon:0,time:1000});
                        window.history.go(-1);
                    }
                }
            });
            return false;

        });
    }),
        /*友情链接管理*/
        i.render({
            elem: "#LAY-friend-url-list",
            height: 450, //设置高度
            url: "/api/url",
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'row' //每页数据量的参数名，默认：limit
            },
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
                    {field: "name",title: "链接名称"},
                    {field: "url", title: "url链接"},
                    {field: "classify", title: "分类", sort: !0},
                    {field: "status",title: "状态",templet:"#statusTpl"},
                    {field: "remark", title: "简介"},
                    {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-friend-url-right-tools"}
                ]
            ],
            page: true,
            limit: 10,
            /*height: "full-220",*/
            text: {
                none: '暂无相关数据', //默认：无数据。注：该属性为 layui 2.2.5 开始新增
                error: '对不起，加载异常'
            }
        }), i.on("tool(LAY-friend-url-list)", function (e) {
        e.data;
        var id = e.data.id;
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {

            admin.req({
                url: "/api/url/deleteone/"+id,
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        e.del(), layer.close(t);
                        layer.msg(data.msg,{offset: '15px',icon:1,time: 1000},function () {
                            layer.close(index); //关闭弹层
                            table.reload('LAY-friend-url-list');
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
                anim: 1,
                type: 2,
                title: "编辑",
                content: "/url/edit/"+id,
                data:[""],
                area: ["600px", "480px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e],
                        r = t.find("iframe").contents().find("#LAY-url-edit-submit");
                    l.layui.form.on("submit(LAY-url-edit-submit)", function (data) {
                        if(data.field.status == "on") {
                            data.field.status = "1";
                        } else {
                            data.field.status = "0";
                        }
                        var field = data.field; //获取提交的字段
                        var url = JSON.stringify(field);
                        admin.req({
                            url:"/api/url",
                            type:"PUT",
                            dataType: "json",
                            contentType: "application/json",
                            data:url
                            ,done: function(data){
                                if(data.code ==0){
                                    var index = parent.layer.getFrameIndex(window.name);
                                    layer.msg(data.msg,{icon:1,time: 1000},function () {
                                        layer.close(index); //关闭弹层
                                        i.reload('LAY-friend-url-list');
                                        window.location.reload();
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
    }),
        /*敏感词管理*/
        i.render({
            elem: "#LAY-word-list",
            height: 450 //设置高度
            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '提示'
                ,layEvent: 'LAYTABLE_TIPS'
                ,icon: 'layui-icon-tips'
            }],
            url: "/api/word/",
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'row' //每页数据量的参数名，默认：limit
            },
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
                    {title: '序号',templet: '#xuhao',width:80},
                    {field: "id", title: "ID",width:80, sort: !0},
                    {field: "word",title: "名称",edit: 'text',style:'font-weight:bold'},
                    {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-word-right-tools"}
                ]
            ],
            page: true,
            limit: 10,
            /*height: "full-220",*/
            text: {
                none: '暂无相关数据', //默认：无数据。注：该属性为 layui 2.2.5 开始新增
                error: '对不起，加载异常'
            }
        }), i.on("tool(LAY-word-list)", function (e) {
        e.data;
        var id = e.data.id;
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {

            admin.req({
                url: "/api/word/deleteone/"+id,
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        e.del(), layer.close(t);
                        layer.msg(data.msg,{offset: '15px',icon:1,time: 1000},function () {
                            layer.close(index); //关闭弹层
                            table.reload('LAY-word-list');
                        })
                    }else {
                        layer.msg(data.msg,{icon:0,time:1000});
                        window.history.go(-1);
                    }
                }
            });
            return false;
        });
    }),

    /*轮播图管理*/
    i.render({
        elem: "#LAY-slide-list",
        height: 450, //设置高度
        url: "/api/module/slide",
        request: {
            pageName: 'page' //页码的参数名称，默认：page
            ,limitName: 'row' //每页数据量的参数名，默认：limit
        },
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
                {title: '序号',templet: '#xuhao',width:80},
                {field: "id", title: "ID",width:80, sort: !0},
                {field: "photo",title: "图标", align: "center",templet:"#imgTemplate"},
                {field: "status",title: "状态",templet:"#statusTpl"},
                {field: "createTime",title: "创建时间"},
                {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-slide-tools"}
            ]
        ],
        page: true,
        limit: 10,
        /*height: "full-220",*/
        text: {
            none: '暂无相关数据', //默认：无数据。注：该属性为 layui 2.2.5 开始新增
            error: '对不起，加载异常'
        }
    }), i.on("tool(LAY-slide-list)", function (e) {
        e.data;
        var id = e.data.id;
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {

            admin.req({
                url: "/api/module/slide/deleteone/"+id,
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        e.del(), layer.close(t);
                        layer.msg(data.msg,{offset: '15px',icon:1,time: 1000},function () {
                            layer.close(index); //关闭弹层
                            table.reload('LAY-slide-list');
                        })
                    }else {
                        layer.msg(data.msg,{icon:0,time:1000});
                        window.history.go(-1);
                    }
                }
            });
            return false;
        });
    }),
        /*公告管理*/
    i.render({
        elem: "#LAY-announce-list",
        url: "/api/announcement",
        request: {
            pageName: 'page' //页码的参数名称，默认：page
            ,limitName: 'row' //每页数据量的参数名，默认：limit
        },
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
                {title: '序号',width:60,templet: '#xuhao'},
                {field: "content",title: "内容"},
                {field: "status",title: "状态",templet:"#statusTpl"},
                {field: "createTime",title: "操作时间"},
                {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-announce-right-tools"}
            ]
        ],
        page: true,
        limit: 10,
        height: 450, //设置高度
        text: {
            none: '暂无相关数据', //默认：无数据。注：该属性为 layui 2.2.5 开始新增
            error: '对不起，加载异常'
        }
    }), i.on("tool(LAY-announce-list)", function (e) {
        e.data;var id = e.data.id;
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {
            admin.req({
                url: "/api/announcement/deleteone/"+id,
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        console.log(data.msg);
                        e.del(), layer.close(t);
                        layer.msg(data.msg,{icon:1,time: 1000},function () {
                            i.reload('LAY-announce-list');
                        })
                    }else {
                        layer.msg(data.msg,{icon:0,time:1000});
                    }
                }
            });
            return false;

        });
    })
        ,e("mgameAdmin", {})
});