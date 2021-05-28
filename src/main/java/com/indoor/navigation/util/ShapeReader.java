package com.indoor.navigation.util;

import com.indoor.navigation.entity.ShapeModel;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author HaoYu
 * @description get shapefile attributes
 * @date 2021/05/24
 */
@Component
public class ShapeReader {
    public ArrayList<ShapeModel> readShapeFile(String filePath) {
        ArrayList<ShapeModel> modelList = new ArrayList<>();
        File folder = new File(filePath);
        if (!folder.isDirectory()) {
            if (folder.toString().endsWith(".shp")) {
                try {
                    modelList = getShapeFile(folder);
                    return modelList;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("选择的文件后缀名不是.shp");
            }
        }else{
            File[] files = folder.listFiles();
            if (!(files.length > 0)) {
                System.out.println("目录文件为空");
                return modelList;
            }

            for (File file : files) {
                if (!file.toString().endsWith(".shp")) {
                    continue;
                }
                try {
                    ArrayList<ShapeModel> models = getShapeFile(file);
                    modelList.addAll(models);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return modelList;
    }

    public ArrayList<ShapeModel> readShapeFileNew(String filePath) {
        ArrayList<ShapeModel> modelList = new ArrayList<>();
        File folder = new File(filePath);
        if (!folder.isDirectory()) {
            if (folder.toString().endsWith(".shp")) {
                try {
                    modelList = getShapeFile(folder);
                    return modelList;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("选择的文件后缀名不是.shp");
            }
        }else{
            File[] files = folder.listFiles();
            if (files == null) {
                return modelList;
            }
            for (File file : files) {
                System.out.println(file.toString());
            }
        }
        return modelList;
    }

    private ArrayList<ShapeModel> getShapeFile(File file) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("url", file.toURI().toURL());
        ArrayList<ShapeModel> models = new ArrayList<>();
        DataStore dataStore = DataStoreFinder.getDataStore(map);
        //字符转码，防止中文乱码
        ((ShapefileDataStore) dataStore).setCharset(Charset.forName("utf8"));
        String typeName = dataStore.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();
        FeatureIterator<SimpleFeature> features = collection.features();
        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            ShapeModel model = new ShapeModel();
            Iterator<? extends Property> iterator = feature.getValue().iterator();
            while (iterator.hasNext()) {
                Property property = iterator.next();
                //property数据与实体类对应
                if (property.getName().toString().equals("the_geom"))
                    model.setThe_geom(property.getValue().toString());
                if (property.getName().toString().equals("id"))
                    model.setId(Integer.parseInt(property.getValue().toString()));
                if (property.getName().toString().equals("BeginId"))
                    model.setBeginId(property.getValue().toString());
                if (property.getName().toString().equals("BeginX"))
                    model.setBeginX(Double.parseDouble(property.getValue().toString()));
                if (property.getName().toString().equals("BeginY"))
                    model.setBeginY(Double.parseDouble(property.getValue().toString()));
                if (property.getName().toString().equals("EndId"))
                    model.setEndId(property.getValue().toString());
                if (property.getName().toString().equals("EndX"))
                    model.setEndX(Double.parseDouble(property.getValue().toString()));
                if (property.getName().toString().equals("EndY"))
                    model.setEndY(Double.parseDouble(property.getValue().toString()));
            }
            models.add(model);
        }
        return models;
    }

    public static void main(String[] args) {
//        ShapeReader shpReader = new ShapeReader();
//        String filePath = "F:\\indoorData\\indoorMap";
//        ArrayList<ShapeModel> modelList = shpReader.readShapeFileNew(filePath);
        Map<String, Integer> dataIndexToVerticesIndex;

    }
}
