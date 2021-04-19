#include "bits/stdc++.h"

using namespace std;

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

map<pair<int, int>, bool> memo;

int n, k;
string s;

bool dp(int ind, int kx) {
	if(ind == n) {
		return kx == k || kx == -k;
	}else if(kx == k || kx == -k) {
		return false;
	}
	pair<int, int> key(ind, kx);
	if(memo.count(key)) {
		return memo[key];
	}
	if(s[ind] != '?') {
		int add = 1;
		if(s[ind] == 'L') {
			add = -1;
		}else if(s[ind] == 'D') {
			add = 0;
		}
		bool ans = dp(ind + 1, kx + add);
		memo[key] = ans;
		return ans;
	}
	for(int i = -1; i <= 1; i++) {
		if(dp(ind + 1, kx + i)) {
			memo[key]= true;
			return true;
		}
	}
	memo[key] = false;
	return false;
}

void solve(int ind, int kx) {
	if(ind == n) {
		assert(kx == k || kx == -k);
		return;
	}else if(kx == k || kx == -k) {
		assert(false);
	}
	pair<int, int> key(ind, kx);
	if(s[ind] != '?') {
		cout << s[ind];
		int add = 1;
		if(s[ind] == 'L') {
			add = -1;
		}else if(s[ind] == 'D') {
			add = 0;
		}
		solve(ind + 1, kx + add);
		return;
	}
	for(int i = -1; i <= 1; i++) {
		if(dp(ind + 1, kx + i)) {
			cout << "LDW"[i + 1];
			solve(ind + 1, kx + i);
			return;
		}
	}
	assert(false);
}

void solve() {
	cin >> n >> k;
	cin >> s;
	if(dp(0, 0)) {
		solve(0, 0);
		cout << endl;
	}else {
		cout << "NO" << endl;
	}
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	int t = 1;
//	cin >> t;
	for(int _ = 1; _ <= t; _++) {
		solve();
	}
}
