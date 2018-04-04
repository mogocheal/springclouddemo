package com.example.cmiss.model.swan;

/**
 * Created by lm on 2017/5/23.
 */
public class SwanData {
    final int RGates = 400; //S BAND 反射率距离库数
    final int VGates = 920; //S BAND 速度距离库数	the maximum resolution is 0.5Km.
    final int WGates = 920; //S BAND 谱宽距离库数	the maximum resolution is 0.5Km.
    final int MaxCuts = 20; //最大仰角层数
    final int MaxRads = 360;//每层仰角上的方位数,每度保留一个径向
    final int CODE_INVALID = 0;//编码值中的特殊标记,表示无有效观测数据
    final int CODE_RANFOLD = 1;//编码值中的特殊标记,表示有距离模糊
    final int VALUE_INVALID = -999;//实际值中的特殊标记,表示无有效观测数据
    final int VALUE_RANFOLD = 999;//实际值中的特殊标记,表示有距离模糊
    final int RES_POINT_FIVE = 2;//速度精度类型,代表的精度为0.5 M/S
    final int RES_ONE_POINT = 4;//速度精度类型,代表的精度为1.0 M/S
    final int VOL_BEG = 3;//体扫开始状态标志
    final int VOL_END = 4;//体扫结束状态标志
    final int ELV_BEG = 0;//仰角开始状态标志
    final int ELV_END = 2;//仰角结束状态标志

    public short[] getTemp1() {
        return temp1;
    }

    public void setTemp1(short[] temp1) {
        this.temp1 = temp1;
    }

    public void setRadarStatus(short radarStatus) {
        RadarStatus = radarStatus;
    }

    public short[] getTemp2() {
        return temp2;
    }

    public void setTemp2(short[] temp2) {
        this.temp2 = temp2;
    }

    final double RADIAN = 3.14159 / 180;
    //头开始
    public short[] temp1 = new short[7]; //保留
    public short RadarStatus;   //1表示为雷达数据
    public short[] temp2 = new short[6];//保留字段
    //数据开始
    public int mSeconds;                        //径向数据收集时间
    public short JulianDate;                        //从1970/1/1起的日期
    public short URange;                            //不模糊距离
    public short Az;                                //方位角度
    public short RadialNumber;                    //径向数据序号
    public short RadialStatus;                    //径向数据状态
    public short El;                                //仰角
    public short ElNumber;                        //体扫内的仰角编号
    public short RangeToFirstGateOfRef;            //第一个反射率数据表示的实际距离(m)		实际为250m
    public short RangeToFirstGateOfDop;            //第一个多普勒数据表示的实际距离(m)		实际为1000m
    public short GateSizeOfReflectivity;            //反射率数据的距离库长(m)
    public short GateSizeOfDoppler;                //多普勒数据的距离库长(m)
    public short GatesNumberOfReflectivity;        //反射率数据的距离库数
    public short GatesNumberOfDoppler;            //多普勒数据的距离库数
    public short CutSectorNumber;                //扇区号
    public int CalibrationConst;                //标定常数
    public short PtrOfReflectivity;                //反射率数据指针(NOTE:相对于基数据部分)
    public short PtrOfVelocity;                    //速度数据指针(NOTE:相对于基数据部分)
    public short PtrOfSpectrumWidth;                //谱宽数据指针(NOTE:相对于基数据部分)
    public short ResolutionOfVelocity;            //多普勒速度分辨率
    public short VcpNumber;                        //体扫号
    public short temp4[]=new short[4];                        //保留
    public short PtrOfArcReflectivity;            //反射率数据指针
    public short PtrOfArcVelocity;                //速度数据指针
    public short PtrOfArcWidth;                    //谱宽数据指针
    public short Nyquist;                        //不模糊速度
    public short temp46;                         //保留
    public short temp47;                         //保留
    public short temp48;                         //保留
    public short CircleTotal;                    //仰角数
    public char temp5[]=new char[30];                        //保留
    public char Echodata[]=new char[RGates+VGates+WGates];    //129－588 共460字节反射率数据
    public char temp[]=new char[4];                        //保留


    public int getRadarStatus() {
        return RadarStatus;
    }



    public int getmSeconds() {
        return mSeconds;
    }

    public void setmSeconds(int mSeconds) {
        this.mSeconds = mSeconds;
    }

    public short getJulianDate() {
        return JulianDate;
    }

    public void setJulianDate(short julianDate) {
        JulianDate = julianDate;
    }

    public short getURange() {
        return URange;
    }

    public void setURange(short URange) {
        this.URange = URange;
    }

    public short getAz() {
        return Az;
    }

    public void setAz(short az) {
        Az = az;
    }

    public short getRadialNumber() {
        return RadialNumber;
    }

    public void setRadialNumber(short radialNumber) {
        RadialNumber = radialNumber;
    }

    public short getRadialStatus() {
        return RadialStatus;
    }

    public void setRadialStatus(short radialStatus) {
        RadialStatus = radialStatus;
    }

    public short getEl() {
        return El;
    }

    public void setEl(short el) {
        El = el;
    }

    public short getElNumber() {
        return ElNumber;
    }

    public void setElNumber(short elNumber) {
        ElNumber = elNumber;
    }

    public short getRangeToFirstGateOfRef() {
        return RangeToFirstGateOfRef;
    }

    public void setRangeToFirstGateOfRef(short rangeToFirstGateOfRef) {
        RangeToFirstGateOfRef = rangeToFirstGateOfRef;
    }

    public short getRangeToFirstGateOfDop() {
        return RangeToFirstGateOfDop;
    }

    public void setRangeToFirstGateOfDop(short rangeToFirstGateOfDop) {
        RangeToFirstGateOfDop = rangeToFirstGateOfDop;
    }

    public short getGateSizeOfReflectivity() {
        return GateSizeOfReflectivity;
    }

    public void setGateSizeOfReflectivity(short gateSizeOfReflectivity) {
        GateSizeOfReflectivity = gateSizeOfReflectivity;
    }

    public short getGateSizeOfDoppler() {
        return GateSizeOfDoppler;
    }

    public void setGateSizeOfDoppler(short gateSizeOfDoppler) {
        GateSizeOfDoppler = gateSizeOfDoppler;
    }

    public short getGatesNumberOfReflectivity() {
        return GatesNumberOfReflectivity;
    }

    public void setGatesNumberOfReflectivity(short gatesNumberOfReflectivity) {
        GatesNumberOfReflectivity = gatesNumberOfReflectivity;
    }

    public short getGatesNumberOfDoppler() {
        return GatesNumberOfDoppler;
    }

    public void setGatesNumberOfDoppler(short gatesNumberOfDoppler) {
        GatesNumberOfDoppler = gatesNumberOfDoppler;
    }

    public short getCutSectorNumber() {
        return CutSectorNumber;
    }

    public void setCutSectorNumber(short cutSectorNumber) {
        CutSectorNumber = cutSectorNumber;
    }

    public int getCalibrationConst() {
        return CalibrationConst;
    }

    public void setCalibrationConst(int calibrationConst) {
        CalibrationConst = calibrationConst;
    }

    public short getPtrOfReflectivity() {
        return PtrOfReflectivity;
    }

    public void setPtrOfReflectivity(short ptrOfReflectivity) {
        PtrOfReflectivity = ptrOfReflectivity;
    }

    public short getPtrOfVelocity() {
        return PtrOfVelocity;
    }

    public void setPtrOfVelocity(short ptrOfVelocity) {
        PtrOfVelocity = ptrOfVelocity;
    }

    public short getPtrOfSpectrumWidth() {
        return PtrOfSpectrumWidth;
    }

    public void setPtrOfSpectrumWidth(short ptrOfSpectrumWidth) {
        PtrOfSpectrumWidth = ptrOfSpectrumWidth;
    }

    public short getResolutionOfVelocity() {
        return ResolutionOfVelocity;
    }

    public void setResolutionOfVelocity(short resolutionOfVelocity) {
        ResolutionOfVelocity = resolutionOfVelocity;
    }

    public short getVcpNumber() {
        return VcpNumber;
    }

    public void setVcpNumber(short vcpNumber) {
        VcpNumber = vcpNumber;
    }

    public short[] getTemp4() {
        return temp4;
    }

    public void setTemp4(short[] temp4) {
        this.temp4 = temp4;
    }

    public short getPtrOfArcReflectivity() {
        return PtrOfArcReflectivity;
    }

    public void setPtrOfArcReflectivity(short ptrOfArcReflectivity) {
        PtrOfArcReflectivity = ptrOfArcReflectivity;
    }

    public short getPtrOfArcVelocity() {
        return PtrOfArcVelocity;
    }

    public void setPtrOfArcVelocity(short ptrOfArcVelocity) {
        PtrOfArcVelocity = ptrOfArcVelocity;
    }

    public short getPtrOfArcWidth() {
        return PtrOfArcWidth;
    }

    public void setPtrOfArcWidth(short ptrOfArcWidth) {
        PtrOfArcWidth = ptrOfArcWidth;
    }

    public short getNyquist() {
        return Nyquist;
    }

    public void setNyquist(short nyquist) {
        Nyquist = nyquist;
    }

    public short getTemp46() {
        return temp46;
    }

    public void setTemp46(short temp46) {
        this.temp46 = temp46;
    }

    public short getTemp47() {
        return temp47;
    }

    public void setTemp47(short temp47) {
        this.temp47 = temp47;
    }

    public short getTemp48() {
        return temp48;
    }

    public void setTemp48(short temp48) {
        this.temp48 = temp48;
    }

    public short getCircleTotal() {
        return CircleTotal;
    }

    public void setCircleTotal(short circleTotal) {
        CircleTotal = circleTotal;
    }

    public char[] getTemp5() {
        return temp5;
    }

    public void setTemp5(char[] temp5) {
        this.temp5 = temp5;
    }

    public char[] getEchodata() {
        return Echodata;
    }

    public void setEchodata(char[] echodata) {
        Echodata = echodata;
    }

    public char[] getTemp() {
        return temp;
    }

    public void setTemp(char[] temp) {
        this.temp = temp;
    }




}
