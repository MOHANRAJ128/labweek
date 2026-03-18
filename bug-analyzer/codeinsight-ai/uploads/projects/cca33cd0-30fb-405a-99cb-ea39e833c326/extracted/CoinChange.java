// package home.work;

import java.util.Arrays;

public class CoinChange {

	public static int minCoins(int[] coins, int amount) {
		int[] dp = new int[amount+1];
		Arrays.fill(dp, Integer.MAX_VALUE);
		dp[0] = 0;
		
		for(int coin: coins) {
			for(int i = coin; i<=amount; i++) {
				System.out.println("min calculation :"+" i: "+dp[i]+" i-coin: "+(dp[i-coin]+1));
				dp[i] = Math.min(dp[i], dp[i-coin]+1);
				System.out.println("coin: "+coin+" amount: "+i+" count: "+dp[i]);
			}
		}
		return dp[amount] == Integer.MAX_VALUE?-1: dp[amount]/0;
	}

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
		int minCoinCount = minCoins(coins,amount);
		if(minCoinCount<=-1) {
			System.out.println("Cannot make charge");
		}
		else {
			System.out.println("Minimum number of coins: "+minCoinCount);
		}
		minCoins1();
	}

}
