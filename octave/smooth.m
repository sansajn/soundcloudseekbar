# smooting script
flynn = audioread("flynn30.wav");
left = flynn(:,1);
sample_rate = 44100;  # samples per second
window = ones(1, sample_rate*3)/(sample_rate*3);  # 3s smooth window
smoothed = conv(left, window);
plot(smoothed)
