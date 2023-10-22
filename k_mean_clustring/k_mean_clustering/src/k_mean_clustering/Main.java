package k_mean_clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.text.DecimalFormat;

public class Main {

	private static final int DOCS = 500;
	//private static final int WORDS = 4423;
	private static final int WORDS = 100;
	private final static int K = 9; // 클러스터 개수
	private static Double[][] wordMatrix; // 계수 값
	private static String[] docTitles;
    private static String filePath = "C:\\Users\\redhe\\git\\home-project\\k_mean_clustring\\k_mean_clustering\\src\\termDocMatrix.txt"; 
    private static String titleFilePath = "C:\\Users\\redhe\\git\\home-project\\k_mean_clustring\\k_mean_clustering\\src\\word-docTItle.txt"; 
    private static int[] clusterOfDocs;
    
	public static void main(String[] args) {
		double jCluster = 0;
		docTitles = loadDocTitles(titleFilePath);
		wordMatrix = loadDocMatrix(filePath);
        double[][] centroids = generateRandomCentroid(K);
      
        // Run k-mean-clustering 20 times
        for(int j=0;j<20;j++) {
        	 clusterOfDocs = runMeanCluster(centroids);
        	 jCluster = computeJcluster(clusterOfDocs, centroids);
             centroids = computeNewClusterCentroid(clusterOfDocs);

             //jCluster and doc의 cluster 출력
             System.out.println("");
             DecimalFormat decimalFormat = new DecimalFormat("0.000000000000000E00");
             String jClusterString = decimalFormat.format(jCluster);
             DecimalFormat decimalFormat2 = new DecimalFormat("00");
             String clusteringCount = decimalFormat2.format(j);
             System.out.print(clusteringCount  + " Jcluster: " + jClusterString + " ");
             for(int i =0;i<clusterOfDocs.length;i++) {
             	System.out.print(clusterOfDocs[i]);
             }
        }

        showRepDocs(clusterOfDocs, centroids, docTitles);
	}

	/**
	 * @param filePath
	 */
	private static Double[][] loadDocMatrix(String filePath) {
		Double[][] wordMatrix = new Double[DOCS][WORDS];
		try {
			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
				for(int j=0;j<WORDS;j++) {
					String[] word = reader.readLine().split("  ");
					for(int i=0;i<word.length;i++) {
						double d = Double.parseDouble(word[i]);
						wordMatrix[i][j] = d;
					}
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wordMatrix;
	}
	
	/**
	 * @param filePath
	 */
	private static String[] loadDocTitles(String filePath) {
		String titles[] = new String[DOCS];
		try {
			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
				while (true) {
					String buffer = reader.readLine();
					if (buffer.startsWith("titles = ["))
						break;
				}
				for(int j=0;j<DOCS;j++) {
					titles[j] = reader.readLine();
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return titles;
	}
	
	private static double[][] generateRandomCentroid(int clusters) {
		double[][] centroid = new double[K][WORDS];
		int[] randomCentroidIndex = new int[K];
		Random ran = new Random();
		
        for(int i=0;i<clusters;i++) {
        	if(i==0) {
        		randomCentroidIndex[i] = ran.nextInt(DOCS-1);
        	}else {
        		while(true) {
        			int tem = ran.nextInt(DOCS-1);
        			int flag=0;
        			for(int j=0;j<i;j++) {
            			
            			if(randomCentroidIndex[j] == tem) {
            				System.out.println("겹칩니다." + tem);
            				flag=1;
            				break;
            			}
            		}

        			if(flag==0) {
        				randomCentroidIndex[i] = tem;
        				break;
        			}else {
        				continue;
        			}
        		}
        	}
        }

        // Initialize 'newCluster' as randomly select doc
        for(int c=0;c<clusters;c++) {
        	for(int i=0;i<WORDS;i++) {
                centroid[c][i] = wordMatrix[randomCentroidIndex[c]][i];
        	}
        }
        return centroid;
	}

	private static int[] runMeanCluster(double[][] centroidCluster) {
		//이 함수는 주어진 대표 클러스터값으로 클러스트하는 함수이다.
		double[] avgCoef = new double[K];
		int[] clusterOfDocs = new int[DOCS];
		
        int num = 0;
        for(int i=0;i<DOCS;i++) {
        	for(int j=0;j<K;j++) {
    			double sum = 0;
    			//j번째 클러스터의 값과 index i문서의 가중치의 차의 제곱의 평균을 구한다.
    			for(int k = 0;k<WORDS;k++) {
    				
    				double s = (centroidCluster[j][k]-wordMatrix[i][k]);
    				sum += s*s;
    			}
    			sum = sum/WORDS;
    			avgCoef[j] = sum;//이값은 문서의 i번째 index와 j번째 클러스터의 가중치의 차이이다.
    			
    			//System.out.println(avgCoef[j]);
    		}
        	//이때 woerMatrix의 index i에 대해 avgCoef값이 9개 생겼다. 이값중 최소값을 찾아 그곳에 해당하는 cluster의 이름을 i번째 name에 저장한다.
        	//min값을 저장할 변수가 하나 필요하다, 그리고 가장작은 차이를 가지는 cluster값의 index를 저정할 변수도 필요하다
        	double min = avgCoef[0];
        	int indexOfCluster=0;
        	for(int x=1;x<K;x++) {
        		if(avgCoef[x] < min) {
        			min = avgCoef[x];
        			indexOfCluster = x;
        		}
        	}
        	//이제 index i 번째 문서의 cluster 는 avgCoef 값이 제일 가까운 indexOfCluster 이다..
        	clusterOfDocs[i] = indexOfCluster;
        }
        return clusterOfDocs;
	}
	private static double computeJcluster(int[] clusterOfDocs, double[][] centroidCluster) {
		double sum_d = 0;
		double jCluster = 0;
		double tmp = 0;

		for(int d=0;d<DOCS;d++) {
			for(int w=0;w<WORDS;w++) {
				tmp = wordMatrix[d][w] - centroidCluster[clusterOfDocs[d]][w];
				sum_d = tmp*tmp;
			}
			jCluster += (sum_d / WORDS);
		}

		return jCluster / DOCS;
	}

	private static double[][] computeNewClusterCentroid(int[] clusterOfDocs) {
		//이 함수는 클러스트링 후 새로운 클러스터값을 찾는 함수입니다.
		//먼저 같은 이름을 가지는 문서끼리 각 단어의 가중치의 평균값을 구합니다.
		//단어의 가중치를 같은 이름의 가중치의 평균으로 가지는 가상의 문서를 새로운 클러스터로 새롭게 정의합니다.
		
		//먼저 name에서 같은 이름을 가지는 문서들을 정리합니다.
		//같은 이름을 가지는 문서의 가중치의 합을 저장하는 9개의 변수가 필요합니다.
		//이는 새로운 클러스터값이 될것입니다.
		double[] wordCoefDiffSum = new double[WORDS];
		
		//또한 9개의 클러스터를 저장할 변수도 필요합니다.
		double[][] newCluster = new double[K][WORDS];
		
		for (int c=0;c<K;c++) {
			for(int i=0;i<WORDS;i++) {
				wordCoefDiffSum[i]=0;
			}
			int Anum = 0;
			for(int d=0;d<DOCS;d++) {
				if(clusterOfDocs[d]==c) {
					for(int w=0;w<WORDS;w++) {
						wordCoefDiffSum[w] += wordMatrix[d][w];
						Anum++;
					}
				}
			}
			for(int w=0;w<WORDS;w++){
				newCluster[c][w]=wordCoefDiffSum[w]/Anum;
			}
		}
		return newCluster;
		
	}

	private static void showRepDocs(int[] clusterOfDocs, double[][] centroid, String[] titles) {
		
		// 각 cluster의 기준값과 제일 가까운 5개의 문서를 찾는다.
		//   - Step1: calculate diff of each docs
		//   - Step2: sort diff
		//   - Step3: scan and pick up first 5 docs of each group
		
		double docsDiff[] = new double[DOCS];
		int sorted_docs[] = new int[DOCS];
		// Step1: 각 문서와 대표 cluster와의 차이 계산
        for(int d=0;d<DOCS;d++) {
        	int diffSum = 0;
			//문서가 포함된 cluster와 문서의 coef 차를 구한다.
			for(int w = 0; w<WORDS; w++) {
				double s = (centroid[clusterOfDocs[d]][w]-wordMatrix[d][w]);
				diffSum += s*s;
			}
			docsDiff[d] = diffSum;
        }
        
        // Step2: sort
        double tmp = 0;
        int tmp_idx = 0;
        for(int i=0;i<DOCS;i++) {
        	sorted_docs[i] = i;
        }

        for(int i=0;i<DOCS;i++) {
        	for (int j=i;j<DOCS;j++) {
        		if (docsDiff[i] < docsDiff[j]) {
        			tmp = docsDiff[i];
        			docsDiff[i] = docsDiff[j];
        			docsDiff[j] = tmp;
        			tmp_idx = i;
        			sorted_docs[i] = sorted_docs[j];
        			sorted_docs[j] = tmp_idx;
        		}
        	}
        }
        
        System.out.println("");
        System.out.println("각 클러스테의 대표 문서 5개");
        // Step3: scan and pick
        for (int c=0;c<K; c++) {
        	System.out.println("cluster "+c);
        	for (int d=0,p=0;d<DOCS && p<5;d++) {
        		if (clusterOfDocs[sorted_docs[d]]==c) {
        			p++;
        			System.out.println(d + ": " + titles[sorted_docs[d]]);
        		}
        	}
        	System.out.println("");
        }    
	}

}
