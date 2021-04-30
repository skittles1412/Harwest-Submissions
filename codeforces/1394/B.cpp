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

const int maxn = 2e5;

int n, m, k, cnt;
long ans = 0;
int bad[100], add[100];
vector<int> vals[maxn], miss[100];

void solve(int ind) {
	if(ind == k) {
		ans += cnt == n;
		return;
	}
	for(int i = 0; i <= ind; i++) {
		if(!bad[ind * 10 + i]) {
			cnt += add[ind * 10 + i];
			for(auto &a: miss[ind * 10 + i]) {
				bad[a]++;
			}
			solve(ind + 1);
			for(auto &a: miss[ind * 10 + i]) {
				bad[a]--;
			}
			cnt -= add[ind * 10 + i];
		}
	}
}

void solve() {
	cin >> n >> m >> k;
	vector<pair<int, int>> graph[n];
	for(int i = 0; i < m; i++) {
		int u, v, w;
		cin >> u >> v >> w;
		u--;
		v--;
		graph[u].emplace_back(v, w);
	}

	for(int i = 0; i < n; i++) {
		sort(begin(graph[i]), end(graph[i]), [](const auto &a, const auto &b) {
			return a.second < b.second;
		});
		for(int j = 0; j < sz(graph[i]); j++) {
			int v = graph[i][j].first;
			vals[v].push_back((sz(graph[i]) - 1) * 10 + j);
		}
	}

	for(int i = 0; i < n; i++) {
		sort(begin(vals[i]), end(vals[i]));
		vals[i].erase(unique(begin(vals[i]), end(vals[i])), vals[i].end());
		for(auto &a: vals[i]) {
			for(auto &b: vals[i]) {
				miss[a].push_back(b);
			}
			add[a]++;
		}
	}

	for(int i = 0; i < 100; i++) {
		sort(begin(miss[i]), end(miss[i]));
		miss[i].erase(unique(begin(miss[i]), end(miss[i])), miss[i].end());
		int bound = (i / 10 + 1) * 10;
		vector<int> nmiss;
		for(auto &a: miss[i]) {
			if(a >= bound) {
				nmiss.push_back(a);
			}
		}
		miss[i] = nmiss;
	}
	
	solve(0);
	cout << ans << endl;
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
