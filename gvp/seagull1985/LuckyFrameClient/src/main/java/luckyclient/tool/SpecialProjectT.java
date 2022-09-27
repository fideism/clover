package luckyclient.tool;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import luckyclient.utils.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author lifengyang
 */
public class SpecialProjectT {
    public static String set_String(String set){
        LogUtil.APP.info("���õ��ַ���Ϊ��"+set);
        return set;
    }


    //����json�ַ����󣬶���key������Ȼ����
    public static String jsonStringNaturalOrdering(String jsonString){
        LogUtil.APP.info("���õ�jsonString�ַ���Ϊ��"+jsonString);
        jsonString=JSONObject.toJSONString(JSONObject.parseObject(jsonString), SerializerFeature.MapSortField);
        LogUtil.APP.info("��Ȼ������jsonString�ַ���Ϊ��"+jsonString);
        return jsonString;
    }

    /**
     * windows ִ��cmd
     * @param cmd
     * @return ����cmd����ִ�н��
     * @throws IOException
     */
    public String exeCMD(String cmd) throws IOException {
        LogUtil.APP.info("cmd����Ϊ��"+cmd);
        String lw=cmd.toLowerCase();
        if(!lw.contains("cmd /c")){
            cmd="cmd /c "+lw;
        }
        //���jmeter ���ɱ���������
        if(cmd.contains("jmeter -g")){
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
            String time=formatter.format(date);
            cmd=cmd+time;
        }
        Process process=Runtime.getRuntime().exec(cmd);
        InputStream input = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input,"GBK"));
        String s;
        StringBuilder sb = new StringBuilder();
        while((s=reader.readLine())!=null){
            sb.append(s+"\n");
        }
        reader.close();
        input.close();
        s=sb.toString();
        LogUtil.APP.info(s);
        return s;
    }
}
