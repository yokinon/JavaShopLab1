package ru.ystu.cmis.util;

import com.sun.jersey.api.view.Viewable;
import lombok.SneakyThrows;
import ru.ystu.cmis.dto.Model;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class Self {
    public static Viewable view(String tplName, Model model){
        return new Viewable(tplName, model.get());
    }
    public static Viewable view(String tplName){

        return new Viewable(tplName);
    }
    @SneakyThrows
    public static String md5(String key){
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(key.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }
}
