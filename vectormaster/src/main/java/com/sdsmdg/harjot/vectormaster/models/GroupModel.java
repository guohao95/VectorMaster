package com.sdsmdg.harjot.vectormaster.models;


import android.graphics.Canvas;
import android.graphics.Matrix;

import com.sdsmdg.harjot.vectormaster.DefaultValues;

import java.util.ArrayList;

public class GroupModel {
    private String name;

    private float rotation;
    private float pivotX, pivotY;
    private float scaleX, scaleY;
    private float translateX, translateY;

    private Matrix scaleMatrix;
    private Matrix originalTransformMatrix;
    private Matrix finalTransformMatrix;
    private GroupModel parent;

    private ArrayList<GroupModel> groupModels;
    private ArrayList<PathModel> pathModels;

    public GroupModel() {
        rotation = DefaultValues.GROUP_ROTATION;
        pivotX = DefaultValues.GROUP_PIVOT_X;
        pivotY = DefaultValues.GROUP_PIVOT_Y;
        scaleX = DefaultValues.GROUP_SCALE_X;
        scaleY = DefaultValues.GROUP_SCALE_Y;
        translateX = DefaultValues.GROUP_TRANSLATE_X;
        translateY = DefaultValues.GROUP_TRANSLATE_Y;

        groupModels = new ArrayList<>();
        pathModels = new ArrayList<>();
    }

    public void drawPaths(Canvas canvas) {
        for (GroupModel groupModel : groupModels) {
            groupModel.drawPaths(canvas);
        }
        for (PathModel pathModel : pathModels) {
            if (pathModel.isFillAndStroke()) {
                pathModel.makeFillPaint();
                canvas.drawPath(pathModel.getPath(), pathModel.getPathPaint());
                pathModel.makeStrokePaint();
                canvas.drawPath(pathModel.getPath(), pathModel.getPathPaint());
            } else {
                canvas.drawPath(pathModel.getPath(), pathModel.getPathPaint());
            }
        }
    }

    public void scaleAllPaths(Matrix scaleMatrix) {
        this.scaleMatrix = scaleMatrix;
        finalTransformMatrix = new Matrix(originalTransformMatrix);
        finalTransformMatrix.postConcat(scaleMatrix);
        for (GroupModel groupModel : groupModels) {
            groupModel.scaleAllPaths(scaleMatrix);
        }
        for (PathModel pathModel : pathModels) {
            pathModel.transform(finalTransformMatrix);
        }
    }

    public void scaleAllStrokeWidth(float ratio) {
        for (GroupModel groupModel : groupModels) {
            groupModel.scaleAllStrokeWidth(ratio);
        }
        for (PathModel pathModel : pathModels) {
            pathModel.setStrokeRatio(ratio);
        }
    }

    public void buildTransformMatrix() {

        originalTransformMatrix = new Matrix();

        originalTransformMatrix.postScale(scaleX, scaleY);
        originalTransformMatrix.postRotate(rotation, pivotX, pivotY);
        originalTransformMatrix.postTranslate(translateX, translateY);

        if (parent != null) {
            originalTransformMatrix.postConcat(parent.getOriginalTransformMatrix());
        }

        for (GroupModel groupModel : groupModels) {
            groupModel.buildTransformMatrix();
        }
    }

    public void updateAndScalePaths() {
        if (scaleMatrix != null) {
            buildTransformMatrix();
            scaleAllPaths(scaleMatrix);
        }
    }

    public GroupModel getGroupModelByName(String name) {
        GroupModel grpModel = null;
        for (GroupModel groupModel : groupModels) {
            if (groupModel.getName().equals(name)) {
                grpModel = groupModel;
                return grpModel;
            } else {
                grpModel = groupModel.getGroupModelByName(name);
                if (grpModel != null)
                    return grpModel;
            }
        }
        return grpModel;
    }

    public PathModel getPathModelByName(String name) {
        PathModel pModel = null;
        for (PathModel pathModel : pathModels) {
            if (pathModel.getName().equals(name)) {
                return pathModel;
            }
        }
        for (GroupModel groupModel : groupModels) {
            pModel = groupModel.getPathModelByName(name);
            if (pModel.getName().equals(name))
                return pModel;
        }
        return pModel;
    }

    public Matrix getOriginalTransformMatrix() {
        return originalTransformMatrix;
    }

    public GroupModel getParent() {
        return parent;
    }

    public void setParent(GroupModel parent) {
        this.parent = parent;
    }

    public void addGroupModel(GroupModel groupModel) {
        groupModels.add(groupModel);
    }

    public ArrayList<GroupModel> getGroupModels() {
        return groupModels;
    }

    public void addPathModel(PathModel pathModel) {
        pathModels.add(pathModel);
    }

    public ArrayList<PathModel> getPathModels() {
        return pathModels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        updateAndScalePaths();
    }

    public float getPivotX() {
        return pivotX;
    }

    public void setPivotX(float pivotX) {
        this.pivotX = pivotX;
    }

    public float getPivotY() {
        return pivotY;
    }

    public void setPivotY(float pivotY) {
        this.pivotY = pivotY;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        updateAndScalePaths();
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        updateAndScalePaths();
    }

    public float getTranslateX() {
        return translateX;
    }

    public void setTranslateX(float translateX) {
        this.translateX = translateX;
        updateAndScalePaths();
    }

    public float getTranslateY() {
        return translateY;
    }

    public void setTranslateY(float translateY) {
        this.translateY = translateY;
        updateAndScalePaths();
    }
}
