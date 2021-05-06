#pragma GCC optimize("Ofast")

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

const int maxn = 2e3 + 2, inf = 0x7f7f7f7f;
const long linf = 1e18;
const int dx[4] {0, 1, 0, -1}, dy[4] {1, 0, -1, 0};

int n, m;
long w;
int qx[maxn * maxn], qy[maxn * maxn], dist1[maxn][maxn], dist2[maxn][maxn], arr[maxn][maxn];

void bfs1() {
	memset(dist1, 0x7f, sizeof(dist1));
	qx[0] = qy[0] = 1;
	int l = 0, r = 1;
	dist1[1][1] = 0;
	while(l < r) {
		int x = qx[l], y = qy[l++], d = dist1[x][y] + 1;
		for(int i = 0; i < 4; i++) {
			int cx = x + dx[i];
			int cy = y + dy[i];
			if(arr[cx][cy] != -1 && dist1[cx][cy] == inf) {
				dist1[cx][cy] = d;
				qx[r] = cx;
				qy[r++] = cy;
			}
		}
	}
}

void bfs2() {
	memset(dist2, 0x7f, sizeof(dist2));
	qx[0] = n;
	qy[0] = m;
	int l = 0, r = 1;
	dist2[n][m] = 0;
	while(l < r) {
		int x = qx[l], y = qy[l++], d = dist2[x][y] + 1;
		for(int i = 0; i < 4; i++) {
			int cx = x + dx[i];
			int cy = y + dy[i];
			if(arr[cx][cy] != -1 && dist2[cx][cy] == inf) {
				dist2[cx][cy] = d;
				qx[r] = cx;
				qy[r++] = cy;
			}
		}
	}
}

void solve() {
	memset(arr, -1, sizeof(arr));
	cin >> n >> m >> w;
	for(int i = 1; i <= n; i++) {
		for(int j = 1; j <= m; j++) {
			cin >> arr[i][j];
		}
	}
	bfs1();
	bfs2();
	long ports = linf, porte = linf;
	for(int i = 0; i < maxn; i++) {
		for(int j = 0; j < maxn; j++) {
			if(arr[i][j] > 0) {
				if(dist1[i][j] != inf) {
					ports = min(ports, arr[i][j] + w * dist1[i][j]);
				}
				if(dist2[i][j] != inf) {
					porte = min(porte, arr[i][j] + w * dist2[i][j]);
				}
			}
		}
	}
	if(max(ports, porte) == linf && dist2[1][1] == inf) {
		cout << -1 << endl;
	}else {
		long ans = linf;
		ans = min(ans, w * dist2[1][1]);
		dbg(ans);
		ans = min(ans, ports + porte);
		dbg(ans);
		cout << ans << endl;
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
