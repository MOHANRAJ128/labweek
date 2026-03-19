package com.service;

import java.util.Arrays;
import java.util.Arrays;

public class UtilityService {
    public static int minCoins(int[] coins, int amount, int divider) {
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
		return dp[amount] == Integer.MAX_VALUE?-1: dp[amount]/divider;
	}
}
