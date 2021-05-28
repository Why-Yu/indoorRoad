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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author HaoYu
 * @description get shapefile attributes
 * @date 2021/05/24
 */
@Component
public class ShapeReader {

    private final static Logger logger = LoggerFactory.getLogger(ShapeReader.class);

    public ArrayList<ShapeModel> readShapeFile(String filePath) {
        ArrayList<ShapeModel> modelList = new ArrayList<>();
        File folder = new File(filePath);
        // 是具体文件就直接检查是否为road.shp，然后读取返回
        if (!folder.isDirectory()) {
            if (folder.toString().endsWith("road.shp")) {
                try {
                    modelList = getShapeFile(folder, "0");
                    return modelList;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                logger.info("请选择shp文件");
            }
        } else {
            // 是目录就要考虑三种情况：空目录、父目录、文件目录
            File[] files = folder.listFiles();
            if (files == null) {
                logger.info("选择的目录是空的");
                return modelList;
            }
            for (File file : files) {
                String floor = "0";
                // 父目录就接着迭代，把models设计成层层传递
                if (file.isDirectory()) {
                    ArrayList<ShapeModel> models = readShapeFile(file.getPath());
                    modelList.addAll(models);
                } else {
                    // 找到具体的road.shp然后读取
                    if (file.getName().endsWith("road.shp")) {
                        try {
                            if (folder.getName().endsWith("F")) {
                                floor = folder.getName().substring(0, folder.getName().length() - 1);
                            }
                            ArrayList<ShapeModel> models = getShapeFile(file, floor);
                            modelList.addAll(models);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return modelList;
    }

    private ArrayList<ShapeModel> getShapeFile(File file, String floor) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("url", file.toURI().toURL());
        ArrayList<ShapeModel> models = new ArrayList<>();
        DataStore dataStore = DataStoreFinder.getDataStore(map);
        //字符转码，防止中文乱码
        ((ShapefileDataStore) dataStore).setCharset(StandardCharsets.UTF_8);
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
                model.setFloor(floor);
            }
            models.add(model);
        }
        return models;
    }

    public static void main(String[] args) {
        String path = "F:\\indoorData\\indoorMap";
        ShapeReader shpReader = new ShapeReader();
        ArrayList<ShapeModel> resultList = shpReader.readShapeFile(path);
        System.out.println(resultList.get(1011));
    }
}
