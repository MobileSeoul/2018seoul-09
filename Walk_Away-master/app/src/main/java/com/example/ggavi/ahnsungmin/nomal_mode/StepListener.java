package com.example.ggavi.ahnsungmin.nomal_mode;

public interface StepListener {

    //This interface will listen to alerts about steps being detected (이 인터페이스는 걸음 수가 감지 됐다는 것을 들을 것)
    public void step(long timeNs);
}
