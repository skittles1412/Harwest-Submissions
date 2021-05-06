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

const int maxn = 2e3;
const int dx[4] {0, 1, 0, -1}, dy[4] {1, 0, -1, 0};

int n, m;
long w;
int qx[maxn * maxn], qy[maxn * maxn], dist1[maxn][maxn], dist2[maxn][maxn], arr[maxn][maxn];

inline bool ibs(int x, int y) {
	return 0 <= x && x < n && 0 <= y && y < m;
}

inline void bfs1() {
	memset(dist1, -1, sizeof(dist1));
	qx[0] = qy[0] = 0;
	int l = 0, r = 1;
	dist1[0][0] = 0;
	while(l < r) {
		int x = qx[l], y = qy[l++], d = dist1[x][y];
		for(int i = 0; i < 4; i++) {
			int cx = x + dx[i];
			int cy = y + dy[i];
			if(ibs(cx, cy) && arr[cx][cy] != -1 && dist1[cx][cy] == -1) {
				dist1[cx][cy] = d + 1;
				qx[r] = cx;
				qy[r++] = cy;
			}
		}
	}
}

inline void bfs2() {
	memset(dist2, -1, sizeof(dist2));
	qx[0] = n - 1;
	qy[0] = m - 1;
	int l = 0, r = 1;
	dist2[n - 1][m - 1] = 0;
	while(l < r) {
		int x = qx[l], y = qy[l++], d = dist2[x][y];
		for(int i = 0; i < 4; i++) {
			int cx = x + dx[i];
			int cy = y + dy[i];
			if(ibs(cx, cy) && arr[cx][cy] != -1 && dist2[cx][cy] == -1) {
				dist2[cx][cy] = d + 1;
				qx[r] = cx;
				qy[r++] = cy;
			}
		}
	}
}

void solve() {
	cin >> n >> m >> w;
	for(int i = 0; i < n; i++) {
		for(int j = 0; j < m; j++) {
			cin >> arr[i][j];
		}
	}
	bfs1();
	bfs2();
	long ports = 1e18, porte = 1e18;
	for(int i = 0; i < n; i++) {
		for(int j = 0; j < m; j++) {
			if(arr[i][j] > 0) {
				if(dist1[i][j] != -1) {
					ports = min(ports, arr[i][j] + dist1[i][j] * w);
				}
				if(dist2[i][j] != -1) {
					porte = min(porte, arr[i][j] + dist2[i][j] * w);
				}
			}
		}
	}
	if(max(ports, porte) == 1e18 && dist2[0][0] == -1) {
		cout << -1 << endl;
	}else {
		long ans = 1e18;
		if(dist2[0][0] != -1) {
			ans = min(ans, dist2[0][0] * w);
		}
		ans = min(ans, ports + porte);
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
