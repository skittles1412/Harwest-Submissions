#pragma GCC optimize("Ofast,O3,no-stack-protector,unroll-loops,fast-math")
#pragma GCC target("fma")

#include "bits/stdc++.h"

using namespace std;

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

void solve() {
	int n, m, k;
	cin >> n >> m >> k;
	if(k & 1) {
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				cout << -1 << " ";
			}
			cout << endl;
		}
		return;
	}

	int rk = k / 2;
	int conv[n][m];
	for(int i = 0; i < n; i++) {
		for(int j = 0; j < m; j++) {
			conv[i][j] = i * m + j;
		}
	}

	int gind[n * m] {};
	pair<int, int> graph[n * m][4];
	for(int i = 0; i < n; i++) {
		for(int j = 0; j < m - 1; j++) {
			int u = conv[i][j], v = conv[i][j + 1], w;
			cin >> w;
			graph[u][gind[u]++] = {v, w};
			graph[v][gind[v]++] = {u, w};
		}
	}
	for(int i = 0; i < n - 1; i++) {
		for(int j = 0; j < m; j++) {
			int u = conv[i][j], v = conv[i + 1][j], w;
			cin >> w;
			graph[u][gind[u]++] = {v, w};
			graph[v][gind[v]++] = {u, w};
		}
	}

	int dist[n * m][rk + 1] {};

	for(int i = 0; i < rk; i++) {
		for(int u = 0; u < n * m; u++) {
			dist[u][i + 1] = 1e9;
			for(auto &[v, w]: graph[u]) {
				if(w > 0) {
					dist[u][i + 1] = min(dist[u][i + 1], w + dist[v][i]);
				}
			}
		}
	}
	for(int i = 0; i < n; i++) {
		for(int j = 0; j < m; j++) {
			cout << dist[conv[i][j]][rk] * 2 << " ";
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
