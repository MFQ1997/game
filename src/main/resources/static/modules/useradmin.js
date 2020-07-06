/** layuiAdmin.std-v1.0.0 LPPL License By http://www.layui.com/admin/ */
;layui.define(["table", "form"], function (e) {
    var t = layui.$, i = layui.table;
    layui.form;
    i.render({
        elem: "#LAY-admin-manage",
        url: "/admin-api",
        cols: [
            [
                {type: "checkbox", fixed: "left"},
                {field: "id", width: 100, title: "ID", sort: !0},
                {field: "uname",title: "用户名",minWidth: 100},
                {field: "phone",title: "手机"},
                {field: "email", title: "邮箱"}, {field: "sex", width: 80, title: "性别"},
                {field: "ip",title: "IP"},
                {field: "jointime", title: "加入时间", sort: !0},
                {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-useradmin-webuser"}
                ]
        ],
        page: !0,
        limit: 30,
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-admin-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.prompt({formType: 1, title: "敏感操作，请验证口令"}, function (t, i) {
            layer.close(i), layer.confirm("真的删除行么", function (t) {
                e.del(), layer.close(t)
            })
        }); else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑用户",
                content: "/amdin/add",
                maxmin: !0,
                area: ["500px", "450px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e], r = "LAY-admin-front-submit",
                        n = t.find("iframe").contents().find("#" + r);
                    l.layui.form.on("submit(" + r + ")", function (t) {
                        t.field;
                        i.reload("LAY-admin-front-submit"), layer.close(e)
                    }), n.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }), i.render({
        elem: "#LAY-admin-back-manage",
        url: "/user-api",
        cols: [
            [
                {type: "checkbox", fixed: "left"},
                {field: "id", width: 80, title: "ID", sort: !0},
                {field: "userName",title: "登录名"},
                {field: "realName", title: "真实名字"},
                {field: "phone", title: "电话"},
                {field: "email", title: "邮箱"},
                {field: "role",title: "角色"},
                {field: "createTime", title: "加入时间", sort: !0},
                {field: "check",title: "审核状态",templet: "#buttonTpl",minWidth: 80,align: "center"},
                {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-useradmin-admin"}]],
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-user-back-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.prompt({formType: 1, title: "敏感操作，请验证口令"}, function (t, i) {
            layer.close(i), layer.confirm("确定删除此管理员？", function (t) {
                console.log(e), e.del(), layer.close(t)
            })
        }); else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑管理员",
                content: "../../../views/user/administrators/adminform.html",
                area: ["420px", "420px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e], r = "LAY-user-back-submit",
                        n = t.find("iframe").contents().find("#" + r);
                    l.layui.form.on("submit(" + r + ")", function (t) {
                        t.field;
                        i.reload("LAY-user-front-submit"), layer.close(e)
                    }), n.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }), i.render({
        /*角色管理*/
        elem: "#LAY-user-back-role",
        url: "/role",
        cols:
            [
                [
                    {type: "checkbox", fixed: "left"},
                    {field: "id", width: 80, title: "ID", sort: !0},
                    {field: "roleName",title: "角色名"},
                    {field: "status", title: "状态",templet: statusTpl},
                    {field: "remark", title: "具体描述"},
                    {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-useradmin-admin"}
                    ]
            ]
        ,page: true
        ,
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-user-back-role)", function (e) {
        e.data;
        var id = e.data.id;
        console.log(e.data);
        if ("del" === e.event) layer.confirm("确定删除此角色？", function (t) {
            e.del(), layer.close(t)
        }); else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑角色",
                content: "/role/editPage?id="+id,
                data:[""],
                area: ["500px", "480px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e],
                        r = t.find("iframe").contents().find("#LAY-user-role-submit");
                    l.layui.form.on("submit(LAY-user-role-submit)", function (t) {
                        t.field;
                        i.reload("LAY-user-back-role"), layer.close(e)
                    }), r.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }), e("useradmin", {})
});