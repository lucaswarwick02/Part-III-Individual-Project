echo "Running Sections 1/2/3"
call run_results.bat
echo "Running Section 4"
call run_heatmap.bat
echo "Running Section 5"
call run_mixed.bat

echo "Running Section 6"
call run_timedependent.bat
echo "Running Section 7"
call run_thresholddependent.bat