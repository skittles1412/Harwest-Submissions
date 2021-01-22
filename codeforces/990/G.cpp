#pragma GCC optimize("Ofast,O3,no-stack-protector,unroll-loops,fast-math")
#pragma GCC target("sse,sse2,sse3,ssse3,sse4,popcnt,abm,mmx,avx,avx2,fma")

#include <bits/stdc++.h>

using namespace std;

#define trav(a, v) for(auto &a: v)

using ll = long long;

const int m = 2e5;

ll sz;
int timer;
int valid[m+1], visited[m+1];
vector<int> graph[m];

void dfs(int u) {
	sz++;
	trav(v, graph[u]) {
		if(valid[v]==timer&&visited[v]!=timer) {
			visited[v] = timer;
			dfs(v);
		}
	}
}

signed main() {
	cin.tie(0)->sync_with_stdio(0);
	int n;
	cin>>n;
	int arr[n];
	vector<int> div[m+1];
	for(int i = 0; i<n; i++) {
		cin>>arr[i];
		div[arr[i]].push_back(i);
	}
	for(int i = 0; i<n-1; i++) {
		int u, v;
		cin>>u>>v;
		u--;
		v--;
		graph[u].push_back(v);
		graph[v].push_back(u);
	}
	ll cnt[m+1];
	memset(valid, 0, sizeof(valid));
	memset(visited, 0, sizeof(visited));
	memset(cnt, 0, sizeof(cnt));
	for(int i = m; i>=1; i--) {
		timer = i;
		for(int j = i; j<=m; j += i) {
			trav(k, div[j]) {
				valid[k] = i;
			}
		}
		for(int j = i; j<=m; j += i) {
			trav(k, div[j]) {
				if(visited[k]!=i) {
					visited[k] = i;
					sz = 0;
					dfs(k);
					cnt[i] += sz*(sz+1)/2;
				}
			}
		}
		for(int j = i*2; j<=m; j += i) {
			cnt[i] -= cnt[j];
		}
	}
	for(int i = 1; i<=m; i++) {
		if(cnt[i]) {
			cout<<i<<" "<<cnt[i]<<"\n";
		}
	}
}
