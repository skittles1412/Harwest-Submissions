#include "bits/stdc++.h"

using namespace std;

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

const int maxn = 500;
int arr[maxn];
int ans[maxn][maxn];

void solve(int n, int add = 0) {
	for(int i = 0; i < n; i++) {
		ans[i][n - 1 - i] = arr[i] + add;
	}
	vector<int> cur;
	for(int i = 0; i < n; i++) {
		if(arr[i] != 1) {
			cur.push_back(arr[i] - 1);
		}
	}
	for(int i = 0; i < n - 1; i++) {
		arr[i] = cur[i];
	}
	if(n > 1) {
		solve(n - 1, add + 1);
	}
}

void solve() {
	int n;
	cin >> n;
	for(int i = 0; i < n; i++) {
		cin >> arr[i];
	}
	solve(n);
	for(int i = n - 1; i >= 0; i--) {
		for(int j = 0; j < n; j++) {
			if(ans[j][i]) {
				cout << ans[j][i] << " ";
			}
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
