#ifdef __CLION_IDE__
#include "bits/stdc++.h"

#else
#include "bits/extc++.h"
#endif

using namespace std;

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

const long inf = 1e14;

//region kactl
namespace {
#define rep(i, a, b) for(int i = a; i < (b); ++i)
#define all(x) begin(x), end(x)
	typedef long ll;
	typedef pair<int, int> pii;
	typedef vector<int> vi;

	//region https://github.com/kth-competitive-programming/kactl/blob/main/content/graph/Dinic.h
	struct Dinic {
		struct Edge {
			int to, rev;
			ll c, oc;

			ll flow() {return max(oc - c, int64_t(0));} // if you need flows
		};

		vi lvl, ptr, q;
		vector<vector<Edge>> adj;

		Dinic(int n): lvl(n), ptr(n), q(n), adj(n) {}

		void addEdge(int a, int b, ll c, ll rcap = 0) {
			adj[a].push_back({b, sz(adj[b]), c, c});
			adj[b].push_back({a, sz(adj[a]) - 1, rcap, rcap});
		}

		ll dfs(int v, int t, ll f) {
			if(v == t || !f)
				return f;
			for(int &i = ptr[v]; i < sz(adj[v]); i++) {
				Edge &e = adj[v][i];
				if(lvl[e.to] == lvl[v] + 1)
					if(ll p = dfs(e.to, t, min(f, e.c))) {
						e.c -= p, adj[e.to][e.rev].c += p;
						return p;
					}
			}
			return 0;
		}

		ll calc(int s, int t) {
			ll flow = 0;
			q[0] = s;
			rep(L, 0, 31)
				do { // 'int L=30' maybe faster for random data
					lvl = ptr = vi(sz(q));
					int qi = 0, qe = lvl[s] = 1;
					while(qi < qe && !lvl[t]) {
						int v = q[qi++];
						for(Edge e : adj[v])
							if(!lvl[e.to] && e.c >> (30 - L))
								q[qe++] = e.to, lvl[e.to] = lvl[v] + 1;
					}
					while(ll p = dfs(s, t, LLONG_MAX))
						flow += p;
				}while(lvl[t]);
			return flow;
		}

		bool leftOfMinCut(int a) {return lvl[a] != 0;}
	};
	//endregion

#undef rep
#undef all
}
//endregion

void solve() {
	int n;
	cin >> n;
	int tconv[2][2] = {{1, 2},
					   {0, 3}};
	int type[n], arr[n][3];
	map<pair<int, int>, int> points;
	long ans = 0;
	for(int i = 0; i < n; i++) {
		cin >> arr[i][0] >> arr[i][1] >> arr[i][2];
		points[{arr[i][0], arr[i][1]}] = i;
		ans += arr[i][2];
		type[i] = tconv[arr[i][0] & 1][arr[i][1] & 1];
		dbg(i, type[i]);
	}

	int cind = 0;
	vector<array<long, 3>> edges;

	int vin[n], vout[n];
	for(int i = 0; i < n; i++) {
		vin[i] = cind++;
		vout[i] = cind++;
		edges.push_back({vin[i], vout[i], arr[i][2]});
	}

	int source = cind++, sink = cind++;

	for(int i = 0; i < n; i++) {
		if(type[i] == 0) {
			edges.push_back({source, vin[i], inf});
		}
	}
	for(int i = 0; i < n; i++) {
		if(type[i] == 3) {
			edges.push_back({vout[i], sink, inf});
		}
	}
	for(int i = 0; i < n; i++) {
		for(int j = 0; j < n; j++) {
			if(type[i] + 1 == type[j] && max(abs(arr[i][0] - arr[j][0]), abs(arr[i][1] - arr[j][1])) <= 1) {
				edges.push_back({vout[i], vin[j], inf});
			}
		}
	}

	Dinic solver(cind);
	dbg(cind);
	set<pair<int, int>> vis;
	for(auto &[u, v, w]: edges) {
		if(!vis.count({u, v})) {
			vis.emplace(u, v);
			dbg(u, v, w);
			solver.addEdge(u, v, w);
		}
	}
	cout << ans - solver.calc(source, sink) << endl;
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
