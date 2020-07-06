;layui.define(["table", "form"], function (e) {
    var t = layui.$, i = layui.table;admin = layui.admin;
    layui.form;
    /*教师管理模块*/
    i.render({
        elem: "#LAY-teacher-list",
        url: "/teacher",
        cols: [
            [
                {type: "checkbox", fixed: "left"},
                {field: "id", width: 100, title: "ID", sort: !0},
                {field: "name",title: "用户名",minWidth: 100},
                {field: "phone",title: "手机"},
                {field: "email", title: "邮箱"},
                {field: "sex", width: 80, title: "性别"},
                {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-teacher-list"}
            ]
        ],
        page: !0,
        limit: 30,
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-teacher-list)", function (e) {
        e.data;
        var id = e.data.id;
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {

            admin.req({
                url: "/teacher",
                data:{"ids":id},
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        console.log(data.message);
                        e.del(), layer.close(t);
                        layer.msg(data.message,{offset: '15px',icon:1,time: 3000},function () {
                            layer.close(index); //关闭弹层
                            table.reload('LAY-teacher-list');
                        })
                    }else {
                        layer.msg(data.msg,{icon:0,time:3000});
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
                content: "/teacher/edit?id="+id,
                data:[""],
                area: ["1000px", "480px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e],
                        r = t.find("iframe").contents().find("#LAY-user-role-submit");
                    l.layui.form.on("submit(LAY-user-role-submit)", function (t) {
                        t.field;
                        i.reload("LAY-teacher-list"), layer.close(e)
                    }), r.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }),
        /*课程管理模块*/
        i.render({
            elem: "#LAY-course-list",
            url: "/teacher",
            cols: [
                [
                    {type: "checkbox", fixed: "left"},
                    {field: "id", width: 100, title: "ID", sort: !0},
                    {field: "name",title: "用户名",minWidth: 100},
                    {field: "phone",title: "手机"},
                    {field: "email", title: "邮箱"},
                    {field: "sex", width: 80, title: "性别"},
                    {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-teacher-list"}
                ]
            ],
            page: !0,
            limit: 30,
            height: "full-220",
            text: "对不起，加载出现异常！"
        }), i.on("tool(LAY-course-list)", function (e) {
        e.data;
        var id = e.data.id;
        console.log(e.data);
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {
            e.del(), layer.close(t)
        }); else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑角色",
                content: "/teacher/edit?id="+id,
                data:[""],
                area: ["1000px", "480px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e],
                        r = t.find("iframe").contents().find("#LAY-user-role-submit");
                    l.layui.form.on("submit(LAY-user-role-submit)", function (t) {
                        t.field;
                        i.reload("LAY-teacher-list"), layer.close(e)
                    }), r.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }),
        /*班级模块*/
    i.render({
        elem: "#LAY-class-list",
        url: "/clazz",
        cols: [
            [
                {type: "checkbox", fixed: "left"},
                {field: "id", width: 100, title: "ID", sort: !0},
                {field: "name",title: "班级",minWidth: 100},
                {field: "num",title: "班级人数"},
                {field: "teacherId", title: "班主任"},
                {field: "xueweiId", width: 80, title: "学委"},
                {field: "tuanzhishuId", width: 80, title: "团支书"},
                {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-class-list"}
            ]
        ],
        page: !0,
        limit: 30,
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-class-list)", function (e) {
        e.data;
        var id = e.data.id;
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {

            admin.req({
                url: "/clazz",
                data:{"ids":id},
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        console.log(data.message);
                        e.del(), layer.close(t);
                        layer.msg(data.message,{offset: '15px',icon:1,time: 3000},function () {
                            layer.close(index); //关闭弹层
                            table.reload('LAY-class-list');
                        })
                    }else {
                        layer.msg(data.msg,{icon:0,time:3000});
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
                content: "/clazz/edit?id="+id,
                data:[""],
                area: ["600px", "400px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e],
                        r = t.find("iframe").contents().find("#LAY-class-front-submit");
                    l.layui.form.on("submit(LAY-class-front-submit)", function (t) {
                        t.field;
                        var field = t.field; //获取提交的字段
                        var clazz = JSON.stringify(field);
                        console.log(clazz);
                        admin.req({
                            url: "/clazz",
                            type:"Put",
                            dataType: "json",
                            contentType: "application/json",
                            data:clazz
                            ,done: function(data){
                                if(data.code ==0){
                                    console.log(data.message);
                                    layer.msg(data.message,{offset: '15px',icon:1,time: 3000},function () {
                                        layer.close(e); //关闭弹层
                                        i.reload("LAY-class-list");
                                    })
                                }else {
                                    layer.msg(data.msg,{icon:0,time:3000});
                                    window.history.go(-1);
                                }
                            }
                        });

                        i.reload("LAY-class-list"), layer.close(e)
                    }), r.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }),/*结束班级模块*/

    /*学生模块*/
    i.render({
        elem: "#LAY-student-list",
        url: "/student",
        cols: [
            [
                {type: "checkbox", fixed: "left"},
                {field: "id", width: 100, title: "ID", sort: !0},
                {field: "stuId",title: "学号",minWidth: 100},
                {field: "name",title: "名字"},
                {field: "sex", title: "性别"},
                {field: "phone", width: 80, title: "电话"},
                {field: "email", width: 80, title: "邮箱"},
                {field: "address", width: 80, title: "地址"},
                {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-student-list"}
            ]
        ],
        page: !0,
        limit: 30,
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-student-list)", function (e) {
        e.data;
        var id = e.data.id;
        /*删除*/
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {

            admin.req({
                url: "/student",
                data:{"ids":id},
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        console.log(data.message);
                        e.del(), layer.close(t);
                        layer.msg(data.message,{offset: '15px',icon:1,time: 3000},function () {
                            layer.close(index); //关闭弹层
                            table.reload('LAY-student-list');
                        })
                    }else {
                        layer.msg(data.msg,{icon:0,time:3000});
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
                content: "/student/edit?id="+id,
                data:[""],
                area: ["800px", "400px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    console.log("提交编辑");
                    var l = window["layui-layer-iframe" + e],
                        r = t.find("iframe").contents().find("#LAY-student-front-submit");
                    l.layui.form.on("submit(LAY-student-front-submit)", function (t) {
                        console.log("开始提交修改的数据");
                        t.field;

                        var field = t.field; //获取提交的字段
                        var student = JSON.stringify(field);
                        console.log("修改的学生的信息是："+student);
                        //提交 Ajax 成功后，静态更新表格中的数据
                        admin.req({
                            url: "/student",
                            type:"Put",
                            dataType: "json",
                            contentType: "application/json",
                            data:student
                            ,done: function(data){
                                if(data.code ==0){
                                    console.log(data.message);
                                    layer.msg(data.message,{offset: '15px',icon:1,time: 3000},function () {
                                        layer.close(e); //关闭弹层
                                        i.reload("LAY-student-list");
                                    })
                                }else {
                                    layer.msg(data.msg,{icon:0,time:3000});
                                    window.history.go(-1);
                                }
                            }
                        });
                        /*$.ajax({
                            url:"/student",
                            /!*async: false,*!/
                            type:"Put",
                            dataType: "json",
                            contentType: "application/json",
                            data:student,
                            success: function(data){
                                if(data.code ==0){
                                    console.log(data.message);
                                    layer.msg(data.message,{offset: '15px',icon:1,time: 3000},function () {
                                        layer.close(index); //关闭弹层
                                        table.reload('LAY-teacher-list');
                                    })
                                }else {
                                    layer.msg(data.msg,{icon:0,time:3000});
                                    window.history.go(-1);
                                }
                            }
                        });*/
                        return false;
                        i.reload("LAY-student-list"), layer.close(e)

                    }), r.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    })/*结束学生模块*/
        ,e("cbms", {})
});