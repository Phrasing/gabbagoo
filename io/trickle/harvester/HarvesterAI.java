/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.ext.web.client.HttpRequest
 *  io.vertx.ext.web.codec.BodyCodec
 */
package io.trickle.harvester;

import io.trickle.core.VertxSingleton;
import io.trickle.util.Pair;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.codec.BodyCodec;

public class HarvesterAI {
    public static HttpRequest sendAPIReq() {
        HttpRequest httpRequest = VertxSingleton.INSTANCE.getLocalClient().getClient().postAbs("https://ai56567.resolved.gg/predict").as(BodyCodec.buffer());
        httpRequest.putHeader("apiKey", "39a0c05dd5cd4fb485ac62711afbad97");
        httpRequest.putHeader("content-type", "application/json");
        httpRequest.putHeader("Accept", "*/*");
        httpRequest.putHeader("Content-Length", "DEFAULT_VALUE");
        return httpRequest;
    }

    public static Pair apiPair(boolean bl, String string) {
        Pair pair = new Pair();
        if (bl) {
            switch (string) {
                case "bicycle": 
                case "bicycles": {
                    string = "bicycle";
                    break;
                }
                case "taxi": 
                case "taxis": {
                    string = "taxi";
                    break;
                }
                case "car": 
                case "cars": {
                    string = "car";
                    break;
                }
                case "motorcycle": 
                case "motorcycles": {
                    string = "motorcycle";
                    break;
                }
                case "bus": 
                case "buses": {
                    string = "bus";
                    break;
                }
                case "train": 
                case "trains": {
                    string = "train";
                    break;
                }
                case "boat": 
                case "boats": {
                    string = "boat";
                    break;
                }
                case "traffic light": 
                case "traffic lights": {
                    string = "traffic light";
                    break;
                }
                case "fire hydrant": 
                case "fire hydrants": 
                case "a fire hydrant": 
                case "hydrants": {
                    string = "fire hydrant";
                    break;
                }
                case "parking meter": 
                case "parking meters": 
                case "meters": {
                    string = "parking meter";
                    break;
                }
                case "crosswalk": 
                case "crosswalks": {
                    string = "crosswalk";
                    break;
                }
                case "vehicle": 
                case "vehicles": {
                    string = "vehicles";
                    break;
                }
            }
        } else {
            switch (string) {
                case "bicycle": 
                case "bicycles": {
                    string = "bicycle";
                    break;
                }
                case "boat": 
                case "boats": {
                    string = "boat";
                    break;
                }
                case "crosswalk": 
                case "crosswalks": {
                    string = "crosswalk";
                    break;
                }
                case "tree": 
                case "trees": {
                    string = "tree";
                    break;
                }
                case "car": 
                case "cars": {
                    string = "car";
                    break;
                }
                case "chimney": 
                case "chimneys": {
                    string = "chimney";
                    break;
                }
                case "fire hydrant": 
                case "fire hydrants": 
                case "a fire hydrant": 
                case "hydrants": {
                    string = "hydrant";
                    break;
                }
                case "motorcycle": 
                case "motorcycles": {
                    string = "motorcycle";
                    break;
                }
                case "statue": 
                case "statues": {
                    string = "statues";
                    break;
                }
                case "stair": 
                case "stairs": {
                    string = "stairs";
                    break;
                }
                case "tractor": 
                case "tractors": {
                    string = "tractor";
                    break;
                }
                case "traffic light": 
                case "traffic lights": {
                    string = "traffic_light";
                    break;
                }
                case "bus": 
                case "buses": {
                    string = "bus";
                    break;
                }
                case "bridge": 
                case "bridges": {
                    string = "bridge";
                    break;
                }
                case "palm tree": 
                case "palm trees": {
                    string = "palm tree";
                    break;
                }
                case "parking meter": 
                case "parking meters": 
                case "meters": {
                    string = "parking_meter";
                    break;
                }
                case "mountains or hills": 
                case "mountain or hill": 
                case "mountains": 
                case "mountain": 
                case "hill": 
                case "hills": {
                    string = "mountains";
                    break;
                }
                case "taxi": 
                case "taxis": {
                    string = "taxi";
                    break;
                }
                case "vehicle": 
                case "vehicles": {
                    string = "vehicles";
                    break;
                }
            }
        }
        pair.first = string;
        if (!bl) {
            pair.second = "YOLO";
            return pair;
        }
        pair.second = "SEGMENTATION";
        return pair;
    }
}

