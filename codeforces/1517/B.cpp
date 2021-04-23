#include "bits/stdc++.h"

using namespace std;

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

void solve() {
	int n, m;
	cin >> n >> m;
	vector<long> arr[n];
	for(int i = 0; i < n; i++) {
		arr[i].resize(m);
		for(int j = 0; j < m; j++) {
			cin >> arr[i][j];
		}
		sort(begin(arr[i]), end(arr[i]));
	}
	long ans[n][m];
	for(int i = 0; i < m; i++) {
		int ind = 0;
		for(int j = 1; j < n; j++) {
			if(arr[j][0] < arr[ind][0]) {
				ind = j;
			}
		}
		for(int j = 0; j < n; j++) {
			auto it = --arr[j].end();
			if(ind == j) {
				it = arr[j].begin();
			}
			ans[j][i] = *it;
			arr[j].erase(it);
		}
	}
	for(int i = 0; i < n; i++) {
		for(int j = 0; j < m; j++) {
			cout << ans[i][j] << " ";
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
	cin >> t;
	for(int _ = 1; _ <= t; _++) {
		solve();
	}
}
