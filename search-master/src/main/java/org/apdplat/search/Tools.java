/**
 *
 * APDPlat - Application Product Development Platform
 * Copyright (c) 2013, 杨尚川, yang-shangchuan@qq.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.apdplat.search;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tools {

    private static final Logger LOG = LoggerFactory.getLogger(Tools.class);

    public static String getHTMLContent(String url) {
        return getHTMLContent(url, "utf-8");
    }

    public static String getHTMLContent(String url, String encoding) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream(),encoding));
            StringBuilder html = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                html.append(line).append("\n");
                line = reader.readLine();
            }
            String content = TextExtract.parse(html.toString());
            return content;
        } catch (Exception e) {
            LOG.debug("解析失敗：" + url, e);
        }
        return null;
    }
    public static void copyFile(InputStream in, File outFile){
        OutputStream out = null;
        try {
            byte[] data=readAll(in);
            out = new FileOutputStream(outFile);
            out.write(data, 0, data.length);
            out.close();
        } catch (IOException ex) {
            LOG.error("ファイル操作失敗",ex);
        } finally {
            try {
                if(in!=null){
                    in.close();
                }
            } catch (IOException ex) {
             LOG.error("ファイル操作失敗",ex);
            }
            try {
                if(out!=null){
                    out.close();
                }
            } catch (IOException ex) {
             LOG.error("ファイル操作失敗",ex);
            }
        }
    }

    public static byte[] readAll(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            for (int n; (n = in.read(buffer)) > 0;) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            LOG.error("読み取り失敗", e);
        }
        return out.toByteArray();
    }
}