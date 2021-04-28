#ifdef __CLION_IDE__
#include "bits/stdc++.h"

#else
#include "bits/extc++.h"
#endif

using namespace std;

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

const int maxn = 2e5;

struct DSU {
	vector<int> par, size;

	explicit DSU(int n): par(n, -1), size(n, 1) {}

	int find(int u) {
		return par[u] < 0 ? u : (par[u] = find(par[u]));
	}

	bool merge(int u, int v) {
		u = find(u);
		v = find(v);
		if(u != v) {
			if(size[u] > size[v]) {
				swap(u, v);
			}
			par[u] = v;
			size[v] += size[u];
			return true;
		}
		return false;
	}
};

int n, m, ccomp;
int color[maxn];
bool vis[maxn];
set<int> unvis;
vector<int> graph[maxn];
vector<array<long, 3>> edges;

void bfs(int s) {
	deque<int> q;
	q.push_front(s);
	unvis.erase(s);
	vis[s] = true;
	while(sz(q)) {
		int u = q.front();
		q.pop_front();
		color[u] = ccomp;
		for(auto &v: graph[u]) {
			unvis.erase(v);
		}
		for(auto &a: unvis) {
			q.push_front(a);
			vis[a] = true;
		}
		unvis.clear();
		for(auto &v: graph[u]) {
			if(!vis[v]) {
				unvis.insert(v);
			}
		}
	}
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	cin >> n >> m;
	long xord = 0;
	for(int i = 0; i < m; i++) {
		long u, v, w;
		cin >> u >> v >> w;
		u--;
		v--;
		xord ^= w;
		graph[u].push_back(v);
		graph[v].push_back(u);
		edges.push_back({u, v, w});
	}
	for(int i = 0; i < n; i++) {
		unvis.insert(i);
	}
	for(int i = 0; i < n; i++) {
		if(unvis.count(i)) {
			bfs(i);
			ccomp++;
		}
	}
	long ans = 0;
	vector<int> used;
	sort(begin(edges), end(edges), [] (const auto &a, const auto &b) {
		return a[2] < b[2];
	});
	{
		DSU dsu(n);
		for(int i = 0; i < m; i++) {
			auto &[u, v, w] = edges[i];
			if(dsu.merge(color[u], color[v])) {
				ans += w;
				used.push_back(i);
			}
		}
	}
	long tote = long(n) * (n - 1) / 2 - m, usede = n - 1 - sz(used);
	if(usede < tote) {
		cout << ans << endl;
	}else {
		assert(usede == tote);
		DSU dsu(n);
		bool iused[m] {};
		for(auto &a: used) {
			dsu.merge(edges[a][0], edges[a][1]);
			iused[a] = true;
		}
		long cans = ans + xord;
		for(int i = 0; i < m; i++) {
			if(!iused[i] && dsu.merge(edges[i][0], edges[i][1])) {
				cans = min(cans, ans + edges[i][2]);
				break;
			}
		}
		cout << cans << endl;
	}
}
