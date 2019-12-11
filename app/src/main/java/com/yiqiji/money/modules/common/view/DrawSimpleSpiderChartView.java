package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class DrawSimpleSpiderChartView extends View {
    private int width;
    private int height;
    private int strokeWidth = 3;
    private Point[] points;
    private Paint paintForOutside;
    private Paint painForText;
    private Rect rect;
    private Canvas canvas;
    private Rect rectForText;
    private Paint paintForCircle;
    private Paint paintForInside;
    public DrawSimpleSpiderChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init();
    }

    public DrawSimpleSpiderChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public DrawSimpleSpiderChartView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        this.canvas = canvas;
        width = getWidth();
        height = getHeight();
        
        
//        Paint paintInPoint = new Paint();
//        paintInPoint.setColor(Color.RED);
//        paintInPoint.setStrokeWidth(2);
//        paintInPoint.setStyle(Style.STROKE);
//        
//        Paint paintInTestPoint = new Paint();
//        paintInTestPoint.setColor(Color.CYAN);
//        paintInTestPoint.setStrokeWidth(2);
//        paintInTestPoint.setStyle(Style.FILL);
//        
        rectForText = new Rect();
        painForText.getTextBounds("5000", 0, 4, rectForText);
        int maginTop = 30;
        int radius = height/2-maginTop;
//        
//        
//        canvas.drawCircle(width/2, height/2, radius, paintInPoint);
        
//        newOne();
        drawPolygon();
//        wrongMakeASpider(canvas, paintInTestPoint, maginTop, radius);
        
    }
    private void drawPolygon(){
//      http://blog.csdn.net/lehtoon_1992/article/details/51036409
        int centerX = width/2;
        int centerY = height/2;
        int maginTop = 30;
        int radius = height/2-maginTop-rectForText.height();
        Path path = new Path();
        float x = centerX;
        float y = centerY-radius;
        path.moveTo(centerX,centerY-radius);
        canvas.drawCircle(centerX, centerY-radius, 3, paintForOutside);
        points[0].initData("收入能力", x, y);
        
        x = (float) (centerX+radius*Math.sin(Math.toRadians(72)));
        y = (float) (centerY-radius*Math.cos(Math.toRadians(72)));
        path.lineTo(x,y);
        points[1].initData("企业授信", x, y);
        canvas.drawCircle(x, y, 3, paintForOutside);
        
        
        x = (float) (centerX+radius*Math.sin(Math.toRadians(36)));
        y = (float) (centerY+radius*Math.cos(Math.toRadians(36)));
        path.lineTo(x,y);
        canvas.drawCircle(x, y, 3, paintForOutside);
        points[2].initData("征信记录", x, y);
     
       x = (float) (centerX-radius*Math.sin(Math.toRadians(36)));
       y = (float) (centerY+radius*Math.cos(Math.toRadians(36)));
       path.lineTo(x,y);
       points[3].initData("财富特征",x, y);
       canvas.drawCircle(x, y, 3, paintForOutside);
       
       
       x = (float) (centerX-radius*Math.sin(Math.toRadians(72)));
       y = (float) (centerY-radius*Math.cos(Math.toRadians(72)));
       path.lineTo(x,y);
       points[4].initData("身份特质", x, y);
       canvas.drawCircle(x, y, 3, paintForOutside);
        
       path.close();
       canvas.drawPath(path, paintForOutside);
       
       int radiusForInsideCircle = radius/3;
       
       double lineWidth = Math.sqrt(Math.pow(centerX-points[0].centerX, 2)+Math.pow(centerY-points[0].centerY, 2));
       lineWidth = lineWidth-radiusForInsideCircle;
       
       int top = 200;
       int right = 200;
       int bottom_right = 200;
       int bottom_left = 200;
       int left = 200;
       
       double total = 1000.0;
       
       double top_percent = top/total;
       double right_percent = right/total;
       double bottom_right_percent = bottom_right/total;
       double bottom_left_percent = bottom_left/total;
       double left_percent = left/total;
       
       float topX = Float.valueOf((top_percent*lineWidth)+"")+radiusForInsideCircle;
       float rightX = Float.valueOf((right_percent*lineWidth)+"")+radiusForInsideCircle;
       float bottom_rightX = Float.valueOf((bottom_right_percent*lineWidth)+"")+radiusForInsideCircle;
       float bottom_leftX = Float.valueOf((bottom_left_percent*lineWidth)+"")+radiusForInsideCircle;
       float leftX = Float.valueOf((left_percent*lineWidth)+"")+radiusForInsideCircle;
       
       x = centerX;
       y = centerY-topX;
       Path pathForInside = new Path();
       pathForInside.moveTo(x, y);
      
       
       x = (float) (centerX+rightX*Math.sin(Math.toRadians(72)));
       y = (float) (centerY-rightX*Math.cos(Math.toRadians(72)));
       pathForInside.lineTo(x,y);
       
       
       x = (float) (centerX+bottom_rightX*Math.sin(Math.toRadians(36)));
       y = (float) (centerY+bottom_rightX*Math.cos(Math.toRadians(36)));
       pathForInside.lineTo(x,y);
       
       
       x = (float) (centerX-bottom_leftX*Math.sin(Math.toRadians(36)));
       y = (float) (centerY+bottom_leftX*Math.cos(Math.toRadians(36)));
       pathForInside.lineTo(x,y);
       
       x = (float) (centerX-leftX*Math.sin(Math.toRadians(72)));
       y = (float) (centerY-leftX*Math.cos(Math.toRadians(72)));
       pathForInside.lineTo(x,y);
       
       pathForInside.close();
       canvas.drawPath(pathForInside, paintForInside);
       
       for(int i = 0;i<5;i++){
           painForText.setColor(Color.WHITE);
           painForText.setTextAlign(Align.CENTER);
           
           if(i == 1){
               painForText.setTextAlign(Align.LEFT);
           }else if(i == 4){
               painForText.setTextAlign(Align.RIGHT);
           }
           
           float startX = points[i].centerX;
           float statY =  points[i].centerY;
           
           if(i == 0){
               statY -= 10;
           }else if(i == 1){
               startX += 10;
               statY += 10;
           }else if(i == 4){
               startX -= 10;
               statY += 10;
           }else if(i == 2 || i == 3){
               statY += 30;
           }
           
           canvas.drawText(points[i].category, startX, statY, painForText);
           painForText.setColor(Color.parseColor("#CCD1D0"));
           painForText.setStrokeWidth(2);
           canvas.drawLine(centerX, centerY, points[i].centerX, points[i].centerY, painForText);
       }
       canvas.drawCircle(centerX, centerY, radiusForInsideCircle, paintForCircle);
       float startX = centerX;
       float startY = centerY+rectForText.height()/2;
       painForText.setTextAlign(Align.CENTER);
       painForText.setColor(Color.parseColor("#83CBC9"));
       canvas.drawText("500", startX, startY, painForText);
    }
  
    private void wrongMakeASpider(Canvas canvas, Paint paintInTestPoint, int maginTop, int radius) {
        //top point
        canvas.drawCircle(width/2, maginTop, 10, paintInTestPoint);
        points[0].initData("��������", width/2, maginTop);
        
        // left point 
        float leftSide = width/2-Float.valueOf((Math.cos(Math.toRadians(18))*radius)+"");
        float rightSide = leftSide*Float.valueOf(Math.tan(Math.toRadians(36))+"");
        canvas.drawCircle(leftSide, maginTop+rightSide, 10, paintInTestPoint);
        points[4].initData("��ҵ����", leftSide, maginTop+rightSide);
        
        //right point 
        float rightPointLeftSide = width/2+Float.valueOf((Math.cos(Math.toRadians(18))*radius)+"");
        canvas.drawCircle(rightPointLeftSide, maginTop+rightSide, 10, paintInTestPoint);
        points[1].initData("���ż�¼", rightPointLeftSide, maginTop+rightSide);
        
        //bottom left point
        float bottomLeftSide = width/2-Float.valueOf((Math.cos(Math.toRadians(54))*radius)+"");
        float bottomMaginTop = Float.valueOf((Math.sin(Math.toRadians(54))*radius)+"");
        canvas.drawCircle(bottomLeftSide, maginTop+radius+bottomMaginTop, 10, paintInTestPoint);
        points[3].initData("�Ƹ�����", bottomLeftSide, maginTop+radius+bottomMaginTop);
        
        // bottom right point
        float bottomRighgSide = width/2+Float.valueOf((Math.cos(Math.toRadians(54))*radius)+"");
        canvas.drawCircle(bottomRighgSide, maginTop+radius+bottomMaginTop, 10, paintInTestPoint);
        points[2].initData("�������",bottomRighgSide, maginTop+radius+bottomMaginTop);
        
        for(int i = 0;i<5;i++){
            if(i<=3){
                canvas.drawLine(points[i].centerX, points[i].centerY, points[i+1].centerX, points[i+1].centerY, paintForOutside);
            }else{
                canvas.drawLine(points[4].centerX, points[4].centerY, points[0].centerX, points[0].centerY, paintForOutside);
            }
       
            painForText.setTextAlign(Align.CENTER);
            
            if(i == 1){
                painForText.setTextAlign(Align.LEFT);
            }else if(i == 4){
                painForText.setTextAlign(Align.RIGHT);
            }
            
            float startX = points[i].centerX;
            float statY =  points[i].centerY;
            
            if(i == 0){
//                statY -= maginTop;
            }else if(i == 1){
                
            }
            
            canvas.drawText(points[i].category, startX, statY, painForText);
        }
    }
   private void init(){
     paintForOutside = new Paint();
     paintForOutside.setAntiAlias(true);
     paintForOutside.setStyle(Style.STROKE);
     paintForOutside.setStrokeWidth(strokeWidth);
     paintForOutside.setColor(Color.parseColor("#7CBADA"));
     
     paintForInside = new Paint();
     paintForInside.setAntiAlias(true);
     paintForInside.setStyle(Style.STROKE);
     paintForInside.setStrokeWidth(strokeWidth);
     paintForInside.setColor(Color.parseColor("#83CBC9"));
     
     paintForCircle = new Paint();
     paintForCircle.setAntiAlias(true);
     paintForCircle.setColor(Color.WHITE);
     paintForCircle.setStyle(Style.FILL);
     
     painForText = new Paint();
     painForText.setAntiAlias(true);
     painForText.setTextSize(25);
     painForText.setColor(Color.parseColor("#83CBC9"));
     
     points = new Point[5];
     
     for(int i = 0;i<5;i++){
         points[i] = new Point();
     }
   }
    class Point{
        private String category;
        private float centerX;
        private float centerY;
        
        public void initData(String category,float CenterX,float CenterY){
            this.category = category;
            this.centerX = CenterX;
            this.centerY = CenterY;
        }
    }
}
