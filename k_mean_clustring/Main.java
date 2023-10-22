package k_mean_clustring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;



public class Main {

	private static final int DOCS = 500;
	private static final int WORDS = 4423;
//	final char[] CLUSTER_NAME = {'A','B','C','D','E','F','G','H','I'};
	public final static int K = 9; // 클러스터 개수
    public static Double[][] wordMatrix = new Double[DOCS][WORDS]; // 계수 값
    public static int[] cluster = new int[K]; // 클러스터
	
	public static void main(String[] args) {
		Random ran = new Random();

        String filePath = "C:\\Users\\redhe\\OneDrive\\바탕 화면\\java\\k-mean clustring\\src\\k_mean_clustring\\termDocMatrix.txt"; 
        
        
        try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			for(int j=0;j<WORDS;j++) {
				String[] word = reader.readLine().split("  ");
				for(int i=0;i<word.length;i++) {
					double d = Double.parseDouble(word[i]);
					wordMatrix[i][j] = d;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        double[][] newCluster = new double[K][WORDS];
        
        for(int i=0;i<K;i++) {
        	if(i==0) {
        		cluster[i] = ran.nextInt(DOCS-1);
        	}else {
        		while(true) {
        			int tem = ran.nextInt(DOCS-1);
        			int flag=0;
        			for(int j=0;j<i;j++) {
            			
            			if(cluster[j] == tem) {
            				System.out.println("겹칩니다." + tem);
            				flag=1;
            				break;
            			}
            		}
        			
        			if(flag==0) {
        				cluster[i] = tem;
        				break;
        			}else {
        				continue;
        			}
        		}
        	}
        }
        
        System.out.println();

        for(int c=0;c<K;c++) {
        	for(int i=0;i<WORDS;i++) {
                newCluster[c][i] = wordMatrix[cluster[c]][i];
        	}
        }
        
       
        for(int j=0;j<20;j++) {
        	 int[] name = cluster(newCluster);
             //name 출력
             System.out.println(name.length);
             for(int i =0;i<name.length;i++) {
             	System.out.print(name[i]);	
             }
             
             newCluster = newAvg(name);
        }
        
        
        showRepDocs(cluster);
        
	}

	private static void showRepDocs(int[] name) {
		// TODO Auto-generated method stub
		for(int c=0;c<K;c++) {
			for(int i=0;i<DOCS;i++) {
				if(name[i]==c) {
					//같은 이름을 가진 문서중 cluster[c]와 가장 근사한값을 가진 문서 5개를 출력한다.
					//필요한것 : cluster정보, 최소값 5개
					
				}
			}
		}
		
	}

	private static int[] cluster(double[][] newCluster) {
		// TODO Auto-generated method stub
		//이 함수는 첫번째 클러스트링 이후 새로운 클러스터값을 가지고 클러스트하는 클러스트함수이다.
		//때문에 처음엔 문서값중 하나를 클러스터값으로 설정했지만 이후부터는 가상의 클러스터를 이용해 클러스트링하기에 새로운 함수가 필요했다.
		//원리는 Firstcluster화 같다.
		double[] avgCoef = new double[K];
        int[] name = new int[DOCS];
		
        int num = 0;
        for(int i=0;i<DOCS;i++) {
        	for(int j=0;j<K;j++) {
    			double sum = 0;
    			//j번째 클러스터의 값과 index i문서의 가중치의 차의 제곱의 평균을 구한다.
    			for(int k = 0;k<WORDS;k++) {
    				
    				double s = (newCluster[j][k]-wordMatrix[i][k]);
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
        	
        	//wordMatrix의 index i값에 대해 최소값은 cluster의 indexOfCluster번이다.
        	//이제 index i 번째 문서의 이름을 해당 클러스터와 같게 만든다.
        	name[i] = indexOfCluster;
        }
        
        
		return name;
	}

	private static double[][] newAvg(int[] name) {
		// TODO Auto-generated method stub
		//이 함수는 클러스트링 후 새로운 클러스터값을 찾는 함수입니다.
		//먼저 같은 이름을 가지는 문서끼리 각 단어의 가중치의 평균값을 구합니다.
		//단어의 가중치를 같은 이름의 가중치의 평균으로 가지는 가상의 문서를 새로운 클러스터로 새롭게 정의합니다.

		
		//먼저 name에서 같은 이름을 가지는 문서들을 정리합니다.
		//같은 이름을 가지는 문서의 가중치의 합을 저장하는 9개의 변수가 필요합니다.
		//이는 새로운 클러스터값이 될것입니다.
		double[] Acluster = new double[WORDS];
		
		//또한 9개의 클러스터를 저장할 변수도 필요합니다.
		double[][] newCluster = new double[K][WORDS];
		
		for (int c=0;c<K;c++) {
			for(int i=0;i<WORDS;i++) {
				Acluster[i]=0;
			}
			int Anum = 0;
			for(int i=0;i<DOCS;i++) {
				if(name[i]==c) {
					for(int j=0;j<WORDS;j++) {
						Acluster[j] += wordMatrix[i][j];
						Anum++;
					}
				}
			}
			for(int w=0;w<WORDS;w++){
				newCluster[c][w]=Acluster[w]/Anum;
			}
		}
		
		return newCluster;
		
	}

}
