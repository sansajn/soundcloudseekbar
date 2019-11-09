# soundcloud like wave visualization

clear all;

flynn = audioread('the son of flynn.flac');
left = flynn(:,1);

sample_rate = 44100;
length_in_s = length(left) / sample_rate  # in seconds

pavgs = zeros(1, length_in_s);
navgs = zeros(1, length_in_s);

for i=1:length_in_s
   second_slice = left((i-1)*sample_rate+1 : i*sample_rate);
   positive = find(second_slice >= 0);
   pavgs(i) = sum(second_slice(positive)) / length(positive);
   negative = find(second_slice < 0);
   navgs(i) = sum(second_slice(negative)) / length(negative);
end

navgs /= 2;  # no idea how soundcloud made negatives

figure;
plot(pavgs);
hold on;
plot(navgs);
xlabel('t [s]');
title('chop\_wave output');

# letx do some experiments with bar
figure;
bar(pavgs);
hold on;
bar(navgs);
