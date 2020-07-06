;layui.define(["table", "form"], function (e) {
    var t = layui.$, i = layui.table;
    layui.form;admin = layui.admin;
    i.render({
        elem: "#LAY-user-manage",
        url: "/api/user",
        request: {
            pageName: 'page' //页码的参数名称，默认：page
            ,limitName: 'row' //每页数据量的参数名，默认：limit
        },
        parseData: function(res) { //res 即为原始返回的数据
            return {
                "code": 0,
                "msg": res.msg,
                "count": res.count,
                "data": res.data.list //解析数据列表
            };
        },
        cols: [
            [
                {type: "checkbox", fixed: "left"},
                {title: '序号',templet: '#xuhao'},
                {field: "userName",title: "用户名"},
                {field: "email", title: "邮箱"},
                {field: "status", title: "状态",templet:"#statusTpl"},
                {field: "loginTime", title: "时间", sort: !0},
                {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-user-tool"}
            ]
        ],
        page: true,
        limit: 10,
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-user-manage)", function (e) {
        e.data;
        var id = e.data.id;
        if ("del" === e.event) layer.confirm("真的删除行么", function (t) {

            admin.req({
                url: "/api/user/deleteone/"+id,
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        e.del(), layer.close(t);
                        layer.msg(data.msg,{offset: '15px',icon:1,time: 3000},function () {
                            layer.close(index); //关闭弹层
                            table.reload('LAY-article-list');
                        })
                    }else {
                        layer.msg(data.msg,{icon:0,time:3000});
                        window.history.go(-1);
                    }
                }
            });
            return false;
        });
        else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑",
                content: "/user/edit/"+id,
                data:[""],
                area: ['400px', '500px'],
                btn: ["确定", "取消"],
                yes: function(index, layero){
                var iframeWindow = window['layui-layer-iframe'+ index]
                    ,submitID = 'LAY-user-edit-submit'
                    ,submit = layero.find('iframe').contents().find('#'+ submitID)
                    ,roles = layero.find('iframe').contents();
                //监听提交
                iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                    var roleList = [];
                    roles.find('input:checkbox[name="roleList"]:checked').each(function (i) {
                        roleList.push(t(this).val());
                    });

                    console.log(admin);
                    //提交 Ajax 成功后，静态更新表格中的数据
                    t.ajax({
                        url:"/api/user/role/"+id,
                        type:"POST",
                        dataType: "json",
                        contentType: "application/json",
                        data: JSON.stringify(roleList),
                        success: function(data){
                            if(data.code ==0){
                                layer.msg(data.msg,{icon:1,time: 1000},function () {
                                    layer.close(index); //关闭弹层
                                })
                            }else {
                                layer.msg(data.msg,{icon:0,time:1000});
                            }
                        }
                    });
                    return false;
                });

                submit.trigger('click');
            },
                success: function (e, t) {}
            })
        }
    }),
       i.render({
        /*角色管理*/
        elem: "#LAY-user-back-role",
        url: "/api/role",
        request: {
            pageName: 'page' //页码的参数名称，默认：page
            ,limitName: 'row' //每页数据量的参数名，默认：limit
        },
        parseData: function(res) { //res 即为原始返回的数据
            return {
                "code": 0,
                "msg": res.msg,
                "count": res.count,
                "data": res.data.list //解析数据列表
            };
        },
        cols:
            [
                [
                    {type: "checkbox", fixed: "left"},
                    {field: "id", width: 80, title: "ID", sort: !0},
                    {title: '序号',width: 80,templet: '#xuhao'},
                    {field: "roleName",title: "角色名"},
                    {field: "status", title: "状态",templet: statusTpl},
                    {field: "remark", title: "具体描述"},
                    {title: "操作",width: 150,align: "center",fixed: "right", toolbar: "#table-useradmin-admin"}
                ]
            ]
        ,page: true
        ,limit:10,
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-user-back-role)", function (e) {
        e.data;
        var id = e.data.id;
        console.log(e.data);
        if ("del" === e.event) layer.confirm("确定删除此角色？", function (t) {
            admin.req({
                url: "/api/role/deleteone/"+id,
                type:"DELETE"
                ,done: function(data){
                    if(data.code ==0){
                        e.del(), layer.close(t);
                        layer.msg(data.msg,{offset: '15px',icon:1,time: 3000},function () {
                            layer.close(index); //关闭弹层
                            table.reload('LAY-article-list');
                        })
                    }else {
                        layer.msg(data.msg,{icon:0,time:3000});
                        window.history.go(-1);
                    }
                }
            });
            return false;
        });
        else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑角色",
                content: "/role/edit/"+id,
                data:[""],
                area: ["800px", "480px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e],
                        r = t.find("iframe").contents().find("#LAY-role-edit-submit");
                    l.layui.form.on("submit(LAY-role-edit-submit)", function (obj) {
                        var authids = layui.authtree.getChecked(t.find('iframe').contents().find("#LAY-auth-tree-index"));
                        console.log('Choosed authids is', authids);
                        /*obj.field.authids = authids;*/
                        obj.field.permissions = authids;
                        if(obj.field.status == "on") {
                            obj.field.status = "1";
                        } else {
                            obj.field.status = "0";
                        }
                        var role = JSON.stringify(obj.field);
                        console.log("提交的角色的信息是："+role);
                        admin.req({
                            url: "/api/role",
                            type:"PUT",
                            contentType: "application/json",
                            dataType: 'json',
                            data: role
                            ,done: function(data){
                                if(data.code ==0){
                                    layer.msg(data.msg,{icon:1,time: 1000},function () {
                                        layer.close(index); //关闭弹层
                                        console.log("执行回调成功_麦发强");
                                        /*table.reload('LAY-article-list');*/
                                    })
                                }else {
                                    layer.msg(data.msg,{icon:0,time:1000});
                                }
                            }
                        });
                        return false;

                        i.reload("LAY-user-back-role"), layer.close(e)
                    }), r.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }), e("mgameAdminManage", {})
});