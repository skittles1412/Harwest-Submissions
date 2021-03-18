#include "bits/stdc++.h"

using namespace std;

//sad; long long double doesn't exist
using ld = long double;

//imagine a language where int = long
#define long long long

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

void solve() {
	int n;
	cin >> n;
	int tag[n];
	vector<int> tags[n];
	for(int i = 0; i < n; i++) {
		cin >> tag[i];
		tag[i]--;
		tags[tag[i]].push_back(i);
	}
	int arr[n];
	for(int i = 0; i < n; i++) {
		cin >> arr[i];
	}
	long dp[n] {};
	for(int i = 0; i < n; i++) {
		for(int j = i - 1; j >= 0; j--) {
			if(tag[i] != tag[j]) {
				long tmp = dp[i];
				dp[i] = max(dp[i], dp[j] + abs(arr[i] - arr[j]));
				dp[j] = max(dp[j], tmp + abs(arr[i] - arr[j]));
			}
		}
	}
	cout << *max_element(dp, dp + n) << endl;
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	int t;
	cin >> t;
	while(t--) {
		solve();
	}
}
