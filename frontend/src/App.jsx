import { useState } from 'react'

function App() {
  const [url, setUrl] = useState('')
  const [expiryDays, setExpiryDays] = useState(7)
  const [result, setResult] = useState(null)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)
  const [statsCode, setStatsCode] = useState('')
  const [stats, setStats] = useState(null)
  const [copied, setCopied] = useState(false)

  const handleShorten = async () => {
    setLoading(true)
    setError(null)
    setResult(null)
    try {
      const res = await fetch('https://url-shortener-production-cbe8.up.railway.app/api/shorten', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ url, expiryDays })
      })
      if (!res.ok) throw new Error('Invalid URL or server error')
      const data = await res.json()
      setResult(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const handleCopy = () => {
    navigator.clipboard.writeText(result.shortUrl)
    setCopied(true)
    setTimeout(() => setCopied(false), 2000)
  }

  const handleStats = async () => {
    setStats(null)
    setError(null)
    try {
      const res = await fetch('https://url-shortener-production-cbe8.up.railway.app/api/stats/' + statsCode)
      if (!res.ok) throw new Error('Short code not found')
      const data = await res.json()
      setStats(data)
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="min-h-screen bg-gray-950 text-white flex flex-col items-center py-16 px-4">
      <h1 className="text-4xl font-bold text-indigo-400 mb-2">URL Shortener</h1>
      <p className="text-gray-400 mb-10">Shorten any link in seconds</p>

      <div className="bg-gray-900 rounded-2xl p-8 w-full max-w-lg shadow-lg mb-8">
        <h2 className="text-lg font-semibold mb-4 text-gray-200">Shorten a URL</h2>
        <input
          className="w-full bg-gray-800 text-white rounded-lg px-4 py-3 mb-3 outline-none focus:ring-2 focus:ring-indigo-500"
          placeholder="Paste your long URL here..."
          value={url}
          onChange={e => setUrl(e.target.value)}
        />
        <div className="flex items-center gap-3 mb-4">
          <label className="text-gray-400 text-sm">Expiry (days):</label>
          <input
            type="number"
            className="bg-gray-800 text-white rounded-lg px-3 py-2 w-20 outline-none focus:ring-2 focus:ring-indigo-500"
            value={expiryDays}
            onChange={e => setExpiryDays(Number(e.target.value))}
          />
        </div>
        <button
          onClick={handleShorten}
          className="w-full bg-indigo-600 hover:bg-indigo-500 text-white font-semibold py-3 rounded-lg transition"
        >
          {loading ? 'Shortening...' : 'Shorten URL'}
        </button>

        {error && <p className="text-red-400 mt-3 text-sm">{error}</p>}

        {result && (
          <div className="mt-5 bg-gray-800 rounded-lg p-4">
            <p className="text-gray-400 text-sm mb-1">Your short URL:</p>
            <div className="flex items-center justify-between gap-2">
              <span className="text-indigo-400 font-medium break-all">
                {result.shortUrl}
              </span>
              <button
                onClick={handleCopy}
                className="bg-indigo-700 hover:bg-indigo-600 text-white text-sm px-3 py-1 rounded-lg transition"
              >
                {copied ? 'Copied!' : 'Copy'}
              </button>
            </div>
            <p className="text-gray-500 text-xs mt-2">
              Expires: {result.expiresAt ? new Date(result.expiresAt).toLocaleDateString() : 'Never'}
            </p>
          </div>
        )}
      </div>

      <div className="bg-gray-900 rounded-2xl p-8 w-full max-w-lg shadow-lg">
        <h2 className="text-lg font-semibold mb-4 text-gray-200">Check Stats</h2>
        <div className="flex gap-3">
          <input
            className="flex-1 bg-gray-800 text-white rounded-lg px-4 py-3 outline-none focus:ring-2 focus:ring-indigo-500"
            placeholder="Enter short code e.g. dg0IUP"
            value={statsCode}
            onChange={e => setStatsCode(e.target.value)}
          />
          <button
            onClick={handleStats}
            className="bg-indigo-600 hover:bg-indigo-500 text-white font-semibold px-5 rounded-lg transition"
          >
            Check
          </button>
        </div>

        {stats && (
          <div className="mt-5 bg-gray-800 rounded-lg p-4 text-sm text-gray-300 space-y-2">
            <p><span className="text-gray-500">Original URL: </span>{stats.originalUrl}</p>
            <p><span className="text-gray-500">Short Code: </span>{stats.shortCode}</p>
            <p><span className="text-gray-500">Click Count: </span>{stats.clickCount}</p>
            <p><span className="text-gray-500">Created: </span>{new Date(stats.createdAt).toLocaleDateString()}</p>
            <p><span className="text-gray-500">Expires: </span>{stats.expiresAt ? new Date(stats.expiresAt).toLocaleDateString() : 'Never'}</p>
          </div>
        )}
      </div>
    </div>
  )
}

export default App