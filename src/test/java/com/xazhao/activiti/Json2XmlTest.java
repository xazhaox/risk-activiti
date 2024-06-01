package com.xazhao.activiti;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xazhao.enums.JobStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description Created on 2024/04/26.
 * @Author xaZhao
 */

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class Json2XmlTest {


    @Test
    public void json2XmlTest() {

        JSON json = new JSONObject(
                "{\n" +
                        "    \"context\": {\n" +
                        "        \"bot\": null,\n" +
                        "        \"brand\": \"\",\n" +
                        "        \"client\": {\n" +
                        "            \"engine\": \"Blink\",\n" +
                        "            \"engine_version\": \"119.0.0.0\",\n" +
                        "            \"family\": \"Chrome\",\n" +
                        "            \"name\": \"Chrome\",\n" +
                        "            \"short_name\": \"CH\",\n" +
                        "            \"type\": \"browser\",\n" +
                        "            \"version\": \"119.0\"\n" +
                        "        },\n" +
                        "        \"device\": \"desktop\",\n" +
                        "        \"model\": \"\",\n" +
                        "        \"os\": {\n" +
                        "            \"family\": \"Windows\",\n" +
                        "            \"name\": \"Windows\",\n" +
                        "            \"platform\": \"x64\",\n" +
                        "            \"short_name\": \"WIN\",\n" +
                        "            \"version\": \"10\"\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"
        );

        String xmlStr = JSONUtil.toXmlStr(json);

        Document parseXml = XmlUtil.parseXml(xmlStr);

        System.out.println(XmlUtil.format(parseXml));
    }

    @Test
    public void xml2Json() {

        String xmlStr =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<context>\n" +
                        "  <bot>null</bot>\n" +
                        "  <brand/>\n" +
                        "  <client>\n" +
                        "    <engine>Blink</engine>\n" +
                        "    <engine_version>119.0.0.0</engine_version>\n" +
                        "    <family>Chrome</family>\n" +
                        "    <name>Chrome</name>\n" +
                        "    <short_name>CH</short_name>\n" +
                        "    <type>browser</type>\n" +
                        "    <version>119.0</version>\n" +
                        "  </client>\n" +
                        "  <device>desktop</device>\n" +
                        "  <model/>\n" +
                        "  <os>\n" +
                        "    <family>Windows</family>\n" +
                        "    <name>Windows</name>\n" +
                        "    <platform>x64</platform>\n" +
                        "    <short_name>WIN</short_name>\n" +
                        "    <version>10</version>\n" +
                        "  </os>\n" +
                        "</context>\n" +
                        "\n";

        JSONObject fromXml = JSONUtil.parseFromXml(xmlStr);

        System.out.println(fromXml.toStringPretty());
    }

    @Test
    public void enumGenerateTest() {

        // SIGN：会签，ORSIGNED：或签，PENDING：委派

        String sign = JobStatus.getJob("SIGN");
        System.out.println(sign);

        String pending = JobStatus.getJob("PENDING");
        System.out.println(pending);
    }

    @Test
    public void strJoinTest() {

        Set<String> userGroupList = new HashSet<>();
        userGroupList.add("Marry");
        userGroupList.add("Jack");
        userGroupList.add("ZhangSan");
        userGroupList.add("Group");
        userGroupList.add("Service");
        userGroupList.add("009918");

        String join = String.join(",", userGroupList);

        String join1 = StringUtils.join(userGroupList, ",");

        System.out.println(join);
        System.out.println(join1);

    }
}
