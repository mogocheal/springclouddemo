package com.example.cmiss.utils;

import java.awt.*;

/**
 * 花色斑图 上的色标  没一种色板图的色标不一样
 * @author louxi
 *
 */
public class DrowSbtVal {
	public static void Drow(String[] titles, double[] contourValues, Color colors[],
                            int cell_h , int cell_w, String unit, float sx, float sy, Graphics2D g){
		
		for(int i=0;i<colors.length;i++){
			if(i==0){
				if(titles[0].contains("气温实况图")||titles[0].contains("极大风实况图")){
					g.drawString("< "+ (int)contourValues[0]+" "+unit, sx + cell_w*1.2f
							, sy+cell_h*1.2f*i+cell_h*0.5f+g.getFont().getSize()*0.5f);
				}else if(titles[0].contains("能见度实况图")){
					g.drawString("≥ "+ (int)contourValues[0]+" "+unit, sx + cell_w*1.2f
							, sy+cell_h*1.2f*i+cell_h*0.5f+g.getFont().getSize()*0.5f);
				}else{
					g.drawString("无降水 ", sx + cell_w*1.2f
							, sy+cell_h*1.2f*i+cell_h*0.5f+g.getFont().getSize()*0.5f);
				}
			}else if(i==colors.length-1){
				if(titles[0].contains("能见度实况")){
					g.drawString(contourValues[i-1]+"≥ "+ (int)contourValues[i]+" "+unit, sx + cell_w*1.2f
							, sy+cell_h*1.2f*i+cell_h*0.5f+g.getFont().getSize()*0.5f);
				}else{
					g.drawString("≥ "+ (int)contourValues[i-1]+" "+unit, sx + cell_w*1.2f
							, sy+cell_h*1.2f*i+cell_h*0.5f+g.getFont().getSize()*0.5f);
				}
			}
			else {
				if((i == colors.length-2 ||i==colors.length-3)&& titles[0].contains("能见度")){
					g.drawString(contourValues[i-1]+"≥ "+ contourValues[i]+" "+unit, sx + cell_w*1.2f
							, sy+cell_h*1.2f*i+cell_h*0.5f+g.getFont().getSize()*0.5f);
				
				}else{
					if(contourValues[i-1]==0.1){
						g.drawString(contourValues[i-1]+" - "+(int)contourValues[i]+" "+unit
								, sx + cell_w*1.2f, sy+cell_h*1.2f*i+cell_h*0.5f+g.getFont().getSize()*0.5f);
					}else{
						g.drawString((int)contourValues[i-1]+" - "+(int)contourValues[i]+" "+unit
								, sx + cell_w*1.2f, sy+cell_h*1.2f*i+cell_h*0.5f+g.getFont().getSize()*0.5f);
					}
				}
			}
		}
	}
	//画一小时降水的  色标
	public static void Drow_PRE_1h(String[] titles, double[] contourValues, Color colors[],
                                   int cell_h , int cell_w, String unit, float sx, float sy, Graphics2D g){
		
		for(int i=0;i<colors.length;i++){
			if(i==0){
				g.drawString("无降水 ", sx + cell_w*1.2f
						, sy+cell_h*1.2f*i+cell_h*0.5f+g.getFont().getSize()*0.5f);
			}else if(i==colors.length-1){
				g.drawString("≥ "+ (int)contourValues[i-1]+" "+unit, sx + cell_w*1.2f
					, sy+cell_h*1.2f*i+cell_h*0.5f+g.getFont().getSize()*0.5f);
			}else {
				if(i==2){
					g.drawString(contourValues[i-1]+" - "+(int)contourValues[i]+" "+unit
							, sx + cell_w*1.2f, sy+cell_h*1.2f*i+cell_h*0.5f+g.getFont().getSize()*0.5f);
				}else if(i==1){
					g.drawString(contourValues[i-1]+" - "+contourValues[i]+" "+unit
							, sx + cell_w*1.2f, sy+cell_h*1.2f*i+cell_h*0.5f+g.getFont().getSize()*0.5f);
				}else{
					g.drawString((int)contourValues[i-1]+" - "+(int)contourValues[i]+" "+unit
							, sx + cell_w*1.2f, sy+cell_h*1.2f*i+cell_h*0.5f+g.getFont().getSize()*0.5f);
				}
			}
		}
	}
}
