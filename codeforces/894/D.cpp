/**
 * code generated by JHelper
 * More info: https://github.com/AlexeyDmitriev/JHelper
 * @author
 */


#pragma GCC optimize("Ofast")

#include <bits/stdc++.h>

using namespace std;

typedef long long ll;

typedef pair<ll, ll> pl;

typedef vector<ll> vl;
typedef vector<pl> vpl;

#define For(i, a, b) for(int i = a; i<b; i++)
#define f0r(i, n) For(i, 0, n)
#define trav(a, v) for(auto& a: v)

#define pb push_back
#define f first
#define s second
#define ub upper_bound

#define sz(x) (int)x.size()
#define all(x) x.begin(), x.end()
#define rz resize

class DRalphAndHisTourInBinaryCountry {
public:
	vector<vpl> graph;
	vector<vl> dist, psum;
	vector<pl> parent;

	vl dfs(int u, pl v) {
		parent[v.f] = {u, v.s};
		dfs(v.f);
		vl ret(dist[v.f]);
		trav(a, ret) {
			a += v.s;
		}
		return ret;
	}

	void dfs(int u = 0) {
		if(sz(graph[u])==0) {
			dist[u].pb(0);
		}else if(sz(graph[u])==1) {
			vl tmp = dfs(u, graph[u][0]);
			dist[u].pb(0);
			trav(a, tmp) {
				dist[u].pb(a);
			}
		}else {
			vl a = dfs(u, graph[u][0]), b = dfs(u, graph[u][1]);
			int n = sz(a), m = sz(b);
			a.pb(LONG_MAX);
			b.pb(LONG_MAX);
			int i = 0, j = 0;
			dist[u].pb(0);
			f0r(ind, n+m) {
				if(a[i]<b[j]) {
					dist[u].pb(a[i++]);
				}else {
					dist[u].pb(b[j++]);
				}
			}
		}
		psum[u].pb(0);
		ll sum = 0;
		trav(a, dist[u]) {
			psum[u].pb(sum += a);
		}
	}

	ll sum(ll u, ll d) {
		ll x = ub(all(dist[u]), d)-dist[u].begin();
		return d*x-psum[u][x];
	}

	void solve(istream &in, ostream &out) {
		int n, m;
		in>>n>>m;
		graph.rz(n);
		dist.rz(n);
		psum.rz(n);
		parent.rz(n);
		f0r(i, n-1) {
			ll d;
			in>>d;
			graph[i>>1].pb({i+1, d});
		}
		dfs();
		f0r(kase, m) {
			ll x, l;
			in>>x>>l;
			x--;
			ll ans = sum(x, l);
			while(x>0) {
				l -= parent[x].s;
				ans -= sum(x, l-parent[x].s);
				x = parent[x].f;
				ans += sum(x, l);
			}
			out<<ans<<"\n";
		}
	}
};


signed main() {
	ios::sync_with_stdio(false);
	cin.tie(0);
	DRalphAndHisTourInBinaryCountry solver;
	std::istream &in(std::cin);
	std::ostream &out(std::cout);
	solver.solve(in, out);
	return 0;
}