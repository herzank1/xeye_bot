/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.monge.xeye.xeye.Xeye;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProtectionDomain;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author DeliveryExpress
 */
public class Utils {

    public static String toJsonString(Object obj) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(obj);

    }
    
       public static String generateXeyeUIDD() {
            return "XEYE_" + UUID.randomUUID().toString();
        }

        public static String getXeyeUUID(String input) {

            String regex = "XEYE_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);

            while (matcher.find()) {
                return matcher.group();
            }

            return null;

        }



    public static String generateUniqueId() {

        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static String getShortName(String name) {

        String[] parts = name.split(" ");
        String capitals = "";
        for (String s : parts) {
            capitals += s.replaceAll("[^a-zA-Z0-9_-]", "").substring(0, 1);
        }
        return capitals.toUpperCase();

    }

    public static boolean isBoolean(String value) {

        return "true".equals(value.toLowerCase()) || "false".equals(value.toLowerCase())
                || value.equals("0") || value.equals("1");

    }

    public static boolean isNumeric(String str) {
        try {
            double parseDouble = Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    public static boolean isUrl(String text) {
        String regex = "\\b(https?|ftp)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        return text.matches(regex);

    }

    public static String getString(String value) {
        //get value to String with null safe 
        if (value == null) {
            return "null";
        } else {
            return value;
        }

    }

    public static boolean isCoordenate(String text) {

        String regex = "^-? ?(90|[0-8]?\\d)(\\.\\d+)?, *-?(180|1[0-7]\\d|\\d?\\d)(\\.\\d+)?$";
        return text.matches(regex);

    }

    public static boolean isPositiveAnswer(String value) {

        return value.toLowerCase().equals("si")
                || value.toLowerCase().equals("yes")
                || value.toLowerCase().equals("y")
                || value.toLowerCase().equals("1");
    }

    public static String toSHA256(String string) {

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    string.getBytes(StandardCharsets.UTF_8));

            return bytesToHex(encodedhash);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static boolean isPositiveAnser(String text) {

        return text.toLowerCase().equals("si")
                || text.toLowerCase().equals("yes")
                || text.toLowerCase().equals("1")
                || text.toLowerCase().equals("y");
    }

    public static String generateUniqued() {

        return generateUniqueId();

    }

    public static int parseBoolean(String value) {

        if (value.toLowerCase().equals("true")
                || value.equals("1")
                || value.toLowerCase().equals("t")) {
            return 1;
        } else {
            return 0;
        }

    }

    public interface Stickers {

        //🔍📝💻❓🛵❌👍⏱⏰💵🍔🟡🟢🔴📞
        String BIEN = "👍";
        String OK = "✅";
        String CANCELAR = "❌";
        String EXCLAMACION = "‼";
        String UBICACION = "📌";
        String TELEFONO = "📞";
        String DURACION = "⏱";
        String FECHA = "⏰";
        String PESOS = "💵";
        String ORDEN = "📝";
        String BUSCAR = "🔍";
        String COMANDO = "💻";
        String AYUDA = "❓";
        String CLIENTE = "👦";
        String REPARTIDOR = "🛵";
        String RESTAURANTE = "🍔";
        String CIRCULO_VERDE = "🟢";
        String CIRCULO_AMARILLO = "🟡";
        String CIRCULO_ROJO = "🔴";
        String ESTRELLA = "⭐";

    }

    /**
     *
     * @author herz Estructura una lista en forma de libro, diviendo los
     * elementos de las lista en secciones o paginas
     */
    public static class PageListViewer<T> {

        int maxItemsPerPage;
        ArrayList<T> list;

        public PageListViewer(ArrayList<T> list, int maxItemsPerPage) {
            this.maxItemsPerPage = maxItemsPerPage;
            this.list = list;
        }

        public int itemsCount() {
            return list.size();
        }

        public int pageCount() {

            double size = (double) list.size();
            double mipp = (double) (maxItemsPerPage);
            return (int) Math.ceil(size / mipp);

        }

        /**
         * *
         *
         * @param page
         * @return start range of page
         */
        public int getStart(int page) {
            if (page == 1) {
                return 1;

            } else {

                return ((page * maxItemsPerPage) - maxItemsPerPage) + 1;

            }

        }

        /**
         * *
         *
         * @param page
         * @return end range of page
         */
        public int getEnd(int page) {
            if (page == 1) {
                return maxItemsPerPage;

            } else {

                return (page * maxItemsPerPage);

            }

        }

        public <T> ArrayList<T> getPage(int pagenumber) {

            if (pagenumber > 0 && pagenumber <= pageCount()) {

                try {
                    List<T> subList = (List<T>) list.subList(getStart(pagenumber) - 1, getEnd(pagenumber));
                    return new ArrayList<T>(subList);
                } catch (java.lang.IndexOutOfBoundsException e) {

                    List<T> subList = (List<T>) list.subList(getStart(pagenumber) - 1, list.size());
                    return new ArrayList<T>(subList);
                }

            } else {
                return null;
            }

        }

        public <T> ArrayList<T> getPrevPage(int current) {

            return getPage(current - 1);
        }

        public <T> ArrayList<T> getNextPage(int current) {
            return getPage(current + 1);

        }

        public int getItemsPerPage() {

            return this.maxItemsPerPage;
        }

        @Override
        public String toString() {
            return "PageListViewer{" + "maxItemsPerPage=" + maxItemsPerPage
                    + ", size=" + this.list.size() + '}';
        }

    }

    public static class FilesUtils {

        public static void checkFile(String fileName, String initial_data) {

            File file = new File(fileName);
            if (!file.exists()) {
                try {
                    System.out.println(fileName + " not Exist");
                    file.createNewFile();
                    // write code for saving data to the file

                    FileWriter myWriter = new FileWriter(fileName);
                    myWriter.write(initial_data);
                    myWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }

        public static String readJsonFile(String fileName) {

            if (!fileName.contains(".json")) {

                fileName += ".json";
            }
            String content = "";
            try {
                content = new String(Files.readAllBytes(Paths.get(fileName)));
            } catch (IOException ex) {
                System.out.println("No se pudo leer el archvo json " + fileName);
                ex.printStackTrace();
            }

            return content;
        }

        public static void writeJson(String filename, final Object s) {

            String toJson = Utils.toJsonString(s);

            if (filename.contains(".json")) {
                write(filename, "", toJson, false);
            } else {
                write(filename, ".json", toJson, false);
            }

        }

        private static void write(String filename, String extension, final String data, boolean appendLine) {

            try {

                FileWriter fw = new FileWriter(filename + extension, appendLine); //the true will append the new data
                if (appendLine) {
                    fw.write(data + "\n");
                } else {
                    fw.write(data);
                }

                fw.close();

                System.out.println(filename + " saved!");
            } catch (IOException ioe) {
                System.err.println("IOException: " + ioe.getMessage());
            }

        }

        public static String getPath(String filename) throws URISyntaxException, IOException {
            final ProtectionDomain domain;
            final CodeSource source;
            final URL url;
            final URI uri;
            String DirectoryPath;
            String separador_directorios = System.getProperty("file.separator");
            String JarURL;
            File auxiliar;
            domain = Xeye.class.getProtectionDomain();
            source = domain.getCodeSource();
            url = source.getLocation();
            uri = url.toURI();
            JarURL = uri.getPath();
            auxiliar = new File(JarURL);
            //Si es un directorio es que estamos ejecutando desde el IDE. En este caso
            // habrá que buscar el fichero en la carperta  abuela(junto a las carpetas "src" y "target·
            if (auxiliar.isDirectory()) {
                auxiliar = new File(auxiliar.getParentFile().getParentFile().getPath());
                DirectoryPath = auxiliar.getCanonicalPath() + separador_directorios;
            } else {
                JarURL = auxiliar.getCanonicalPath();
                DirectoryPath = JarURL.substring(0, JarURL.lastIndexOf(separador_directorios) + 1);

            }

            System.out.println(DirectoryPath + filename);
            return DirectoryPath + filename;
        }

    }

    public static class JsonUtils {

        /**
         * *
         *
         * @param <T>
         * @param jsonArray
         * @param clazz
         * @return a Array of Objects from JsonArray
         */
        public static <T> ArrayList<T> JsonArrayToObject(String jsonArray, ArrayList<T> clazz) {

            Gson gson = new Gson();
            TypeToken type = new TypeToken<ArrayList<T>>() {
            };

            ArrayList<T> array = gson.fromJson(jsonArray, type.getType());

            return array;

        }

        /**
         * *
         *
         * @param <T>
         * @param jsonArray
         * @param clazz
         * @return a Array of Objects from JsonArray
         */
        public static <T> ArrayList<T> JsonArrayToObject(JsonArray jsonArray, ArrayList<T> clazz) {

            Gson gson = new Gson();
            TypeToken type = new TypeToken<ArrayList<T>>() {
            };

            ArrayList<T> array = gson.fromJson(jsonArray, type.getType());

            return array;

        }

        /**
         * *
         *
         * @param <T>
         * @param clazz
         * @return a JsonElement of custom arraylist of object
         */
        public static <T> JsonElement toJsonArray(ArrayList<T> clazz) {

            Gson gson = new Gson();
            TypeToken type = new TypeToken<ArrayList<T>>() {
            };
            JsonElement element = gson.toJsonTree(clazz, type.getType());
            return element;

        }

        /**
         *
         * @param fileName
         * @return a JsonObject of a file
         */
        public static JsonObject convertFileToJSON(String fileName) {

            // Read from File to String
            JsonObject jsonObject = new JsonObject();

            try {
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(new FileReader(fileName));
                jsonObject = jsonElement.getAsJsonObject();
            } catch (FileNotFoundException e) {

                return null;

            }

            return jsonObject;
        }

    }

    public static class DateUtils {

        public static String getTodayDate() {

            long unixToday = Instant.now().getEpochSecond();
            Instant instant = Instant.ofEpochSecond(unixToday);
            String pattern = "MMMMM dd yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = Date.from(instant);

            return simpleDateFormat.format(date);

        }

        public static String getNowDate() {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date date = new Date();
            return date.toString();
        }

        public static long getUnixTimeStamp() {

            long unixTime = System.currentTimeMillis() / 1000L;
            return unixTime;
        }

        public static int getWeekNumer() {

            Calendar calendar = Calendar.getInstance(Locale.getDefault());

            int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

            return weekOfYear;

        }

        public static long getLastSundayOfThisWeekAt7amUNIX() {

            LocalDateTime loc = LocalDate
                    .now()
                    .with(
                            TemporalAdjusters.previous(DayOfWeek.SUNDAY)
                    ).atTime(7, 0);
            ZoneId zoneId = ZoneId.systemDefault();
            long epoch = loc.atZone(zoneId).toEpochSecond();

            System.out.println("getLastSundayOfThisWeek " + loc.toString());
            System.out.println("getLastSundayOfThisWeek UNIX " + epoch);

            return epoch;

        }

        public static long getTodayAtStartTimeStamp() {

            long todayAtStart = atStartOfDay(new Date());

            return todayAtStart;
        }

        public static long getThisWeekSundayAtStartTimeStamp() {

            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            long thisWeekAtStart = atStartOfDay(c.getTime()); //this week's sunday at 00:00 am
            return thisWeekAtStart;

        }

        public static long getThisMonthAtStartTimeStamp() {
            Calendar md1 = Calendar.getInstance();
            md1.set(Calendar.DAY_OF_MONTH, 1);
            long thisMonthFirstDayAtStart = atStartOfDay(md1.getTime()); //First day of this month at 00:00 am
            return thisMonthFirstDayAtStart;
        }

        public static String secondsToHHMMSS(int seconds) {

            int p1 = seconds % 60;
            int p2 = seconds / 60;
            int p3 = p2 % 60;
            p2 = p2 / 60;

            return p2 + ":" + p3 + ":" + p1;

        }

        public static String unixToDate(long unix) {
            Instant instant = Instant.ofEpochSecond(unix);
            String pattern = "dd/MM/yyyy HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            Date date = Date.from(instant);
            return simpleDateFormat.format(date);
        }

        public static long getTimeElapsedSeconds(long a, long b) {

            return a - b;

        }

        public static long dateToUnix(String creationDate) {

            try {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = dateFormat.parse(creationDate);
                long unixTime = (long) date.getTime() / 1000;
                return unixTime;
            } catch (ParseException ex) {
                Logger.getLogger(DateUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

            return 0;

        }

        public static Long atStartOfDay(Date date) {
            LocalDateTime localDateTime = dateToLocalDateTime(date);
            LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);

            return startOfDay.toEpochSecond(ZoneOffset.UTC);

        }

        public static Long atEndOfDay(Date date) {
            LocalDateTime localDateTime = dateToLocalDateTime(date);
            LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
            return endOfDay.toEpochSecond(ZoneOffset.UTC);
        }

        private static LocalDateTime dateToLocalDateTime(Date date) {
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        }

     
    }

}
