package com.wanniu.core.util;

import com.wanniu.core.GConfig;
import com.wanniu.core.GGlobal;
import com.wanniu.core.logfs.Out;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


public class NameUtil {
    public static Random random = new Random();


    private static List<String> NicknameLib = new ArrayList<>();

    public static final int __COUNT__ = (Locale.getDefault() == Locale.ENGLISH) ? 9 : 6;

    public static final char[] __CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


    public static void loadNickLib() {
        File nickDir = new File(GGlobal.DIR_NICKNAME);
        if (nickDir.exists() && nickDir.isDirectory()) {
            Map<String, Boolean> caches = new HashMap<>();
            File[] langs = nickDir.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    if (file.isFile()) return true;
                    return false;
                }
            });
            List<String> nicknames = null;
            for (File file : langs) {
                nicknames = FileUtil.readLines(file);
                for (String nickname : nicknames) {
                    nickname = nickname.trim();
                    if (nickname.length() > 0 && nickname.length() < __COUNT__) {
                        if (caches.containsKey(nickname)) {
                            Out.warn(new Object[]{"昵称库 (", file.getName(), ") ->  [", nickname, "]  已经存在！"});
                            continue;
                        }
                        NicknameLib.add(nickname);
                        caches.put(nickname, Boolean.TRUE);
                    }
                }

                Out.info(new Object[]{"加载昵称包文件 -> ", file.getName(), "\t\t[", Integer.valueOf(nicknames.size()), " / ", Integer.valueOf(NicknameLib.size()), "]"});
                caches.clear();
            }
        } else {
            Out.error(new Object[]{"昵称库 (", GGlobal.DIR_NICKNAME, ") 不存在！"});
        }
    }


    public static void loadLib(List<String> excludes) {
        loadNickLib();
        removeAll(excludes);
    }


    public static String fromChars() {
        StringBuilder builder = new StringBuilder();
        int amount = 5 + random.nextInt(5);
        for (int i = 0; i < amount; i++) {
            builder.append(__CHARS[random.nextInt(__CHARS.length)]);
        }
        return builder.toString();
    }


    public static String fromLib() {
        if (NicknameLib.size() == 0) return null;
        synchronized (NicknameLib) {
            return NicknameLib.get(random.nextInt(NicknameLib.size()));
        }
    }


    public static void remove(String nickname) {
        synchronized (NicknameLib) {
            NicknameLib.remove(nickname);
        }
    }


    public static void removeAll(List<String> nicknames) {
        synchronized (NicknameLib) {
            NicknameLib.removeAll(nicknames);
        }
    }


    public static String getNickFrom(boolean lib) {
        return lib ? fromLib() : fromChars();
    }


    private static List<String> LastnameLib = new ArrayList<>();

    private static List<String> FirstnameLib0 = new ArrayList<>();

    private static List<String> FirstnameLib1 = new ArrayList<>();

    public static void init() {
        int nickname = GConfig.getInstance().getInt("game.nickname.enable", 0);
        if (nickname == 1) {
            loadNickLib();
        } else if (nickname == 2) {
            loadNameLib();
        }
    }


    public static void loadNameLib() {
        if (LastnameLib.size() > 0) {
            LastnameLib.clear();
            FirstnameLib0.clear();
            FirstnameLib1.clear();
        }
        buildName("lastname.txt", LastnameLib, "百家姓库");
        buildName("firstname_0.txt", FirstnameLib0, "男名字库");
        buildName("firstname_1.txt", FirstnameLib1, "女名字库");
    }

    private static void buildName(String name, List<String> names, String log) {
        File file = new File(GGlobal.DIR_NICKNAME + File.separator + "name" + File.separator + name);
        if (file.exists() && file.isFile()) {
            Map<String, Boolean> caches = new HashMap<>();
            List<String> firstnames = FileUtil.readLines(file);
            for (String firstname : firstnames) {
                firstname = firstname.trim();
                if (firstname.length() > 0 && firstname.length() < __COUNT__) {
                    if (caches.containsKey(firstname)) {
                        Out.warn(new Object[]{log, " (", file.getName(), ") ->  [", firstname, "]  重复存在！"});
                        continue;
                    }
                    names.add(firstname);
                    caches.put(firstname, Boolean.TRUE);
                }
            }

            Out.info(new Object[]{"加载", log, "文件 -> ", file.getName(), "\t\t[", Integer.valueOf(firstnames.size()), "]"});
            caches.clear();
        } else {
            Out.error(new Object[]{log, " (", file.getAbsolutePath(), ") 不存在！"});
        }
    }


    public static String getName(boolean man) {
        return (String) LastnameLib.get(random.nextInt(LastnameLib.size())) + (man ? FirstnameLib0
                .get(random.nextInt(FirstnameLib0.size())) : FirstnameLib1
                .get(random.nextInt(FirstnameLib1.size())));
    }
}


