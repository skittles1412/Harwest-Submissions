#include "bits/stdc++.h"

using namespace std;

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

vector<vector<int>> solve(int n) {
	vector<vector<int>> ans;
	for(int i = 0; i < n; i++) {
		int x = i * 6;
		ans.push_back({x + 1, x + 2, x + 3, x + 5});
	}
	return ans;
}

void solve() {
	int n, k;
	cin >> n >> k;
	auto ans = solve(n);
	int mx = 0;
	for(auto &a: ans) {
		for(auto &b: a) {
			b *= k;
			mx = max(mx, b);
		}
	}
	cout << mx << endl;
	for(auto &a: ans) {
		for(auto &b: a) {
			cout << b << " ";
		}
		cout << endl;
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
