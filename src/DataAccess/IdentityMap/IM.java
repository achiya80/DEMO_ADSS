package DataAccess.IdentityMap;

import DataAccess.PrimaryKeys.PK;

import java.util.HashMap;
import java.util.List;

public class IM {

    private static IM instance;

    private final HashMap<String, Object> counter;

    private final HashMap<String, Object> businessList;

    private IM() {
        counter = new HashMap<>();
        businessList = new HashMap<>();
    }

    public static IM getInstance() {
        if (instance == null) {
            instance = new IM();
        }
        return instance;
    }

    public <T> HashMap<PK, T> getIdentityMap(Class<T> tClass) {
        HashMap<PK, T> im = (HashMap<PK, T>) counter.get(tClass.getName());
        if (im == null) {
            im = new HashMap<>();
            counter.put(tClass.getName(), im);
        }
        return im;
    }


    public <T> List<T> getList(String tClass){
        return  (List<T>) businessList.get(tClass);
    }

    public <T> void setList(String tClass, List<T> lst){
        businessList.put(tClass, lst);
    }

    public <T> void addToList(String tClass, T obj){
        List<T> lst = getList(tClass);
        if(lst!=null){
            lst.add(obj);
        }
    }

    public <T> void removeFromList(String tClass, T obj){
        List<T> lst = getList(tClass);
        if(lst!=null){
            lst.remove(obj);
        }
    }

    public <T> void clearList(String tClass){
        businessList.remove(tClass);
    }

}

