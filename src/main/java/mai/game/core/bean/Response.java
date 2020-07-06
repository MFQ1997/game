package mai.game.core.bean;


import mai.game.core.util.JsonUtil;

/*
* @Description: 这是返回的数据库反馈
* */
public class Response<T> {

    private String msg = "";
    private int code = 0;
    private int count;
    private T data;
    private int[] checked;

    /**
     * 返回一个新的实例
     */
    public static <T> Response<T> newInstance() {
        return new Response<T>();
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int[] getChecked() {
        return checked;
    }

    public void setChecked(int[] checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return JsonUtil.objToJson(this);
    }
}
