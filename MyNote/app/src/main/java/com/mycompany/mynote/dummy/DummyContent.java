//package com.mycompany.mynote.dummy;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Helper class for providing sample content for user interfaces created by
// * Android template wizards.
// * <p/>
// * TODO: Replace all uses of this class before publishing your app.
// */
//public class DummyContent {
//
//    /**
//     * An array of sample (dummy) items.
//     */
//    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();
//
//    /**
//     * A map of sample (dummy) items, by ID.
//     */
//    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
//
//    private static final int COUNT = 25;
//    private static int ids = 0;
//
//    static {
//        // Add some sample items.
//       // for (int i = 1; i <= COUNT; i++) {
//       //     addItem(createDummyItem(i));
//       // }
//    }
//    public static void cleanItem(){
//        ITEMS.clear();
//        ITEM_MAP.clear();
//    }
//    public static void addItem(DummyItem item) {
//        ITEMS.add(item);
//        ITEM_MAP.put(item.id.toString(), item);
//    }
//
//    private static DummyItem createDummyItem(int position) {
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
//        String dateString = formatter.format(date);
//        return new DummyItem(ids++, "Item " + position, makeDetails(position),dateString);
//    }
//
//    private static String makeDetails(int position) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("Details about Item: ").append(position);
//        for (int i = 0; i < position; i++) {
//            builder.append("\nMore details information here.");
//        }
//        return builder.toString();
//    }
//
//    /**
//     * A dummy item representing a piece of content.
//     */
//    public static class DummyItem {
//        public final Integer id;
//        public final String title;
//        public final String date;
//        public final String details;
//
//        public DummyItem(Integer id,String title, String details,String strDate) {
//            this.id = id;
//            this.title = title;
//            this.date = strDate;
//            this.details = details;
//        }
//
//        @Override
//        public String toString() {
//            return title;
//        }
//    }
//}
