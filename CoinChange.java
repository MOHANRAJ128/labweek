import java.util.Arrays;
import com.service.*;

public class CoinChange {

	public static void minCoins1(){
		int[] arr = {2,11};
		int k = 12,max=arr[0],c=0;
		int count=0;
		while(k>=arr[1])
		{
			for(int i=0;i<arr.length;i++)
			{
				if(arr[i]<=k)
				{
					max = arr[i];
				}
			}
			k=k-max;
			c++;
			//System.out.println(max+" "+k+" ");
		}
		int totalmoves = c+k;
		System.out.println("total count : "+ totalmoves);
	}
	
	public static void main(String[] args) {
//		int[] coins = {1,5,10};

		int[] coins = {2,11};
		int amount = 12;
		int divider = 0; // You can set this to any value you want to divide the result by
		int minCoinCount = UtilityService.minCoins(coins, amount, divider);
		if(minCoinCount<=-1) {
			System.out.println("Cannot make charge");
		}
		else {
			System.out.println("Minimum number of coins: "+minCoinCount);
		}
		minCoins1();
	}

}
