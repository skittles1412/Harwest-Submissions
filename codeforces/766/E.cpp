#include "bits/stdc++.h"

using namespace std;

//sad; long long double doesn't exist
using ld = long double;

//imagine a language where int = long
#define long long long

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

const int maxn = 1e5;

long ans = 0;
int t = 0;
int arr[maxn], sz[maxn], tin[maxn], tout[maxn], lift[17][maxn], val[17][maxn];
bool vis[maxn];
vector<int> adj[maxn], graph[maxn];

void pdfs(int u, int p = -1) {
	tin[u] = t++;
	lift[0][u] = p;
	val[0][u] = arr[u];
	for(auto &v: adj[u]) {
		if(v != p) {
			pdfs(v, u);
		}
	}
	tout[u] = t++;
}

bool anc(int u, int v) {
	return tin[u] <= tin[v] && tout[u] >= tout[v];
}

int query(int u, int v) {
	int ans = 0;
	if(!anc(u, v)) {
		for(int i = 16; i >= 0; i--) {
			if(lift[i][u] != -1 && !anc(lift[i][u], v)) {
				ans ^= val[i][u];
				u = lift[i][u];
			}
		}
		ans ^= val[0][u];
		u = lift[0][u];
	}
	if(!anc(v, u)) {
		for(int i = 16; i >= 0; i--) {
			if(lift[i][v] != -1 && !anc(lift[i][v], u)) {
				ans ^= val[i][v];
				v = lift[i][v];
			}
		}
		ans ^= val[0][v];
		v = lift[0][v];
	}
	ans ^= arr[u];
	return ans;
}

void maint(int u, int p = -1) {
	sz[u] = 1;
	for(auto &v: adj[u]) {
		if(!vis[v] && v != p) {
			maint(v, u);
			sz[u] += sz[v];
		}
	}
}

int dcomp(int u) {
	int tar = sz[u] / 2;
	while(true) {
		int max = 0, ind = -1;
		for(auto &v: adj[u]) {
			if(!vis[v] && sz[v] > max) {
				max = sz[v];
				ind = v;
			}
		}
		if(max > tar) {
			sz[u] -= sz[ind];
			sz[ind] += sz[u];
			u = ind;
		}else {
			vis[u] = true;
			for(auto &v: adj[u]) {
				if(!vis[v]) {
					maint(v);
					graph[u].push_back(dcomp(v));
				}
			}
			return u;
		}
	}
}

using ds = array<array<int, 2>, 20>;

void add(ds &a, int x) {
	for(int i = 0; i < 20; i++) {
		a[i][bool((x >> i) & 1)]++;
	}
}

void sub(ds &a, int x) {
	for(int i = 0; i < 20; i++) {
		a[i][bool((x >> i) & 1)]--;
	}
}

void eval(const ds &a, int x) {
	for(int i = 0; i < 20; i++) {
		ans += (1LL << i) * a[i][bool((x >> i) & 1) ^ 1];
	}
}

vector<int> dfs(int u) {
	ds cds {};
	add(cds, 0);
	vector<int> vals;
	vals.push_back(u);
	vector<vector<int>> cvals;
	cvals.reserve(sz(graph[u]));
	for(auto &v: graph[u]) {
		const auto &a = dfs(v);
		vector<int> cur;
		for(auto &x: a) {
			int tmp = arr[u] ^query(u, x);
			add(cds, tmp);
			cur.push_back(tmp);
		}
		cvals.push_back(cur);
		vals.insert(vals.end(), a.begin(), a.end());
	}
	ans += arr[u];
	for(int i = 0; i < sz(graph[u]); i++) {
		for(auto &a: cvals[i]) {
			sub(cds, a);
		}
		for(auto &a: cvals[i]) {
			eval(cds, arr[u] ^ a);
		}
	}
	return vals;
}


int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	int n;
	cin >> n;
	for(int i = 0; i < n; i++) {
		cin >> arr[i];
	}
	for(int i = 0; i < n - 1; i++) {
		int u, v;
		cin >> u >> v;
		u--;
		v--;
		adj[u].push_back(v);
		adj[v].push_back(u);
	}
	pdfs(0);
	for(int i = 1; i < 17; i++) {
		for(int j = 0; j < n; j++) {
			if(lift[i - 1][j] == -1) {
				lift[i][j] = -1;
			}else {
				lift[i][j] = lift[i - 1][lift[i - 1][j]];
				val[i][j] = val[i - 1][lift[i - 1][j]] ^ val[i - 1][j];
			}
		}
	}
	maint(0);
	int root = dcomp(0);
	dfs(root);
	cout << ans << endl;
}
