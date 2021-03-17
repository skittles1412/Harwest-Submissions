#include "bits/stdc++.h"

using namespace std;

//sad; long long double doesn't exist
using ld = long double;

//imagine a language where int = long
#define long long long

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

const int m = 3200;
vector<int> primes;

void init() {
	bool p[m + 1];
	memset(p, 1, sizeof(p));
	for(int i = 2; i <= m; i++) {
		if(p[i]) {
			primes.push_back(i);
			for(int j = i * i; j <= m; j += i) {
				p[j] = false;
			}
		}
	}
}

void solve() {
	int n, k;
	cin >> n >> k;
	int ind = 0;
	int arr[n];
	map<pair<int, vector<int>>, int> conv;
	for(int i = 0; i < n; i++) {
		int x;
		cin >> x;
		vector<int> cur;
		for(auto &a: primes) {
			int cnt = 0;
			while(x % a == 0) {
				x /= a;
				cnt++;
			}
			if(cnt & 1) {
				cur.push_back(a);
			}
		}
		auto res = conv.try_emplace({x, cur}, ind);
		arr[i] = res.first->second;
		if(res.second) {
			ind++;
		}
	}
	vector<int> vals[n];
	for(int i = 0; i < n; i++) {
		vals[arr[i]].push_back(i);
	}
	int inds[n];
	for(int i = 0; i < n; i++) {
		inds[i] = sz(vals[i]);
	}
	set<int> events;
	int dp[n + 1][k + 1] {};
	for(int i = n - 1; i >= 0; i--) {
		inds[arr[i]]--;
		if(inds[arr[i]] + 1 < sz(vals[arr[i]])) {
			events.insert(vals[arr[i]][inds[arr[i]] + 1]);
		}
		for(int j = 0; j <= k; j++) {
			dp[i][j] = 1e9;
			auto it = events.begin();
			for(int a = 0; a <= j; a++) {
				if(it == events.end()) {
					dp[i][j] = 0;
				}else {
					dp[i][j] = min(dp[i][j], dp[*it][j - a] + 1);
					it++;
				}
			}
		}
	}
	cout << dp[0][k] + 1 << endl;
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	init();
	int t;
	cin >> t;
	while(t--) {
		solve();
	}
}
