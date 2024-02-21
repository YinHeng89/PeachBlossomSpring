package com.contacts.peachblossomspring.ui.contacts;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VCFParser {
    /**
     * 解析VCF文件并提取联系人信息
     *
     * @param stream 包含VCF数据的输入流
     * @return 包含联系人信息的列表
     * @throws IOException 如果读取输入流时发生错误
     */
    public List<Contact> parseContacts(InputStream stream) throws IOException {
        List<Contact> contacts = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        String line;
        String name = null;
        String phone = null;
        Set<String> existingPhones = new HashSet<>(); // 存储已存在的电话号码

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("BEGIN:VCARD")) {
                name = null;
                phone = null;
            } else if (line.startsWith("FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:")) {
                name = decodeQuotedPrintable(line.substring(41)); // 解码并保存名字
            } else if (line.startsWith("TEL;CELL:") || line.startsWith("TEL;CELL;PREF:")) {
                phone = line.substring(line.indexOf(":") + 1); // 提取电话号码
                if (existingPhones.contains(phone)) {
                    // 如果电话号码已存在，跳过当前联系人
                    name = null;
                    phone = null;
                } else {
                    existingPhones.add(phone);
                }
            } else if (line.startsWith("END:VCARD")) {
                if (name != null && phone != null) {
                    contacts.add(new Contact(name, phone)); // 将名字和电话号码组成一个新的Contact对象，并添加到联系人列表中
                }
            }
        }

        reader.close();
        return contacts;
    }

    /**
     * 解码Quoted-Printable编码的字符串
     *
     * @param encoded 经过Quoted-Printable编码的字符串
     * @return 解码后的字符串
     */
    private String decodeQuotedPrintable(String encoded) {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        try {
            return codec.decode(encoded).replaceAll("E:", ""); // 解码Quoted-Printable编码的字符串，并移除"E:"
        } catch (DecoderException e) {
            e.printStackTrace();
            return encoded;
        }
    }
}
