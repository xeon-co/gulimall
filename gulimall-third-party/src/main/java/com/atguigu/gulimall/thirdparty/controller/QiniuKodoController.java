package com.atguigu.gulimall.thirdparty.controller;

import com.atguigu.common.utils.R;
import com.atguigu.gulimall.thirdparty.utils.QiniuKodoUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("third/kodo")
public class QiniuKodoController {
    @Resource
    QiniuKodoUtil qiniuKodoUtil;

    // https://blog.csdn.net/dreaming9420/article/details/123599844

    @RequestMapping("upload")
    public R upload(@RequestParam(value = "localFilePath") MultipartFile localFilePath) {
        String ret = qiniuKodoUtil.upload(localFilePath);

        return R.ok().put("key", ret);
    }

    @RequestMapping("listSpaceFiles")
    public void listSpaceFiles() {
        qiniuKodoUtil.listSpaceFiles();
    }

    @RequestMapping("getFileUrl")
    public R getFileUrl(String fileName) {
        String name = "";
        try {
            name = qiniuKodoUtil.getFileUrl(fileName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return R.ok().put("url", name);
    }

    @RequestMapping("deleteFile")
    public R deleteFile(String[] fileList) {
        Boolean isDel = qiniuKodoUtil.deleteFile(fileList);

        return R.ok().put("deleted", isDel);
    }
}
