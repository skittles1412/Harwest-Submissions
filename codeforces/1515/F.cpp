#include "bits/extc++.h"

using namespace std;

template<class T, class U = less<T>>
using rt = __gnu_pbds::tree<T, __gnu_pbds::null_type, U, __gnu_pbds::rb_tree_tag, __gnu_pbds::tree_order_statistics_node_update>;

template<class T, class U>
void sep(T &out, const string &s, const U &u) {
	out << u;
}

template<class T, class Head, class ...Tail>
void sep(T &out, const string &s, const Head &h, const Tail &...t) {
	out << h << s;
	sep(out, s, t...);
}

#ifdef DEBUG
#define dbg(...)                                                      \
cerr << "L" << __LINE__ << " [" << #__VA_ARGS__ << "]" << ": ";       \
sep(cerr, " | ", __VA_ARGS__);                                        \
cerr << endl
#else
#define cerr if(false) cerr
#define dbg(...) cerr
#endif

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

struct DSU {
	vector<int> p;

	explicit DSU(int n): p(n, -1) {}

	int find(int u) {
		return p[u] < 0 ? u : (p[u] = find(p[u]));
	}
	
	bool merge(int u, int v) {
		u = find(u);
		v = find(v);
		if(u == v) {
			return false;
		}
		if(p[u] < p[v]) {
			swap(u, v);
		}
		p[v] += p[u];
		p[u] = v;
		return true;
	}
};

const int maxn = 3e5;

long r, arr[maxn];
vector<pair<int, int>> graph[maxn];
vector<int> front, back;

void dfs(int u, int p = -1, int e = -1) {
	for(auto &[v, i]: graph[u]) {
		if(e != i) {
			dfs(v, u, i);
		}
	}
	if(p != -1) {
		if(arr[p] + arr[u] >= r) {
			arr[p] = arr[p] + arr[u] - r;
			front.push_back(e);
		}else {
			back.push_back(e);
		}
	}
}

void solve() {
	int n, m;
	cin >> n >> m >> r;
	for(int i = 0; i < n; i++) {
		cin >> arr[i];
	}
	if(accumulate(arr, arr + n, 0LL) < (n - 1) * r) {
		cout << "NO" << endl;
		return;
	}
	cout << "YES" << endl;
	DSU dsu(n);
	for(int i = 0; i < m; i++) {
		int u, v;
		cin >> u >> v;
		u--;
		v--;
		if(dsu.merge(u, v)) {
			graph[u].emplace_back(v, i);
			graph[v].emplace_back(u, i);
		}
	}
	dfs(0);
	for(auto &a: front) {
		cout << a + 1 << endl;
	}
	reverse(begin(back), end(back));
	for(auto &a: back) {
		cout << a + 1 << endl;
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
		dbg(_);
//		cout << "Case #" << _ << ": ";
		solve();
	}
}
