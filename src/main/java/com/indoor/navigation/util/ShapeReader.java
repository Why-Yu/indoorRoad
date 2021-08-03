package com.indoor.navigation.util;

import com.indoor.navigation.entity.database.ShapeModel;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                logger.info("检测到xlsx文件或是输入了非shp文件");
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
                    if (file.getName().endsWith("road.shp") || file.getName().endsWith("roads.shp")) {
                        try {
                            if (folder.getName().endsWith("F")) {
                                floor = folder.getName().substring(0, folder.getName().length() - 1);
                            }
                            ArrayList<ShapeModel> models = getShapeFile(file, floor);
                            modelList.addAll(models);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (file.getName().endsWith(".xlsx")) {
                    } else {
                        if (!file.getName().contains("road")) {
                            file.delete();
                        }
                    }
                }
            }
        }
        return modelList;
    }

    public ArrayList<ShapeModel> readSingleShapeFile(String filePath, String floor) {
        ArrayList<ShapeModel> modelList = new ArrayList<>();
        File folder = new File(filePath);
        if (folder.toString().endsWith("road.shp")) {
            try {
                modelList = getShapeFile(folder, floor);
                return modelList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.info("请选择shp文件");
        }
        return modelList;
    }

    private ArrayList<ShapeModel> getShapeFile(File file, String floor) throws Exception {

        Map<String, Object> map = new HashMap<>();
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
            ShapeModel model = new ShapeModel(
                    feature.getAttribute("id").toString(),
                    feature.getAttribute("BeginId").toString(),
                    Double.parseDouble(feature.getAttribute("BeginX").toString()),
                    Double.parseDouble(feature.getAttribute("BeginY").toString()),
                    feature.getAttribute("EndId").toString(),
                    Double.parseDouble(feature.getAttribute("EndX").toString()),
                    Double.parseDouble(feature.getAttribute("EndY").toString()),
                    floor
            );
            models.add(model);
        }
        // 关闭FeatureIterator以及DataStore,防止之后再读取，出现The following locker still has a lock: read on file错误
        // 这里两个都需要关闭，之前有一个未关闭会报错
        features.close();
        dataStore.dispose();
        return models;
    }

    public static void main(String[] args) {
        String path = "E:\\TopoData\\中心百货";
        ShapeReader shpReader = new ShapeReader();
        ArrayList<ShapeModel> resultList = shpReader.readShapeFile(path);
        System.out.println(resultList);
    }
}
