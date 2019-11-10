function wave_heights(wave_file, samples)
   
   wave = audioread(wave_file);
   left = wave(:,1);
   k = numel(left) / samples;
   
   heights = zeros(2, samples);
   for n = 1:samples
      from = floor((n-1)*k+1);
      to = floor(n*k);
      slice = left(from:to);
      
      positives = find(slice > 0);
      heights(1,n) = mean(slice(positives));
      
      negatives = find(slice < 0);
      heights(2,n) = mean(slice(negatives));
   end
      
   heights = heights ./ [
      max(heights(1,:)); 
      min(heights(2,:))
   ];  # also converts negative part to positive
      
   figure;
   plot(heights(1,:));
   hold on;
   plot(heights(2,:)*-1);
   
   # bar plot
   figure;
   bar(heights(1,:));
   hold on;
   bar(heights(2,:)*-1);
   
   imwrite(uint8(heights * 255), 'wave_heights.png');
   
endfunction
