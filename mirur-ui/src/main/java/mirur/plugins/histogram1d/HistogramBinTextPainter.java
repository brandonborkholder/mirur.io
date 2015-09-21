/*
 * This file is part of Mirur.
 *
 * Mirur is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mirur is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mirur.  If not, see <http://www.gnu.org/licenses/>.
 */
package mirur.plugins.histogram1d;

import mirur.core.Array1D;
import mirur.plugins.DataUnitConverter;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.painter.info.SimpleTextPainter;

public class HistogramBinTextPainter extends SimpleTextPainter {
    private final Axis2D srcAxis;
    private HistogramPainter histPainter;
    private Array1D array;
    private double lastBin;
    private DataUnitConverter unitConverter;

    public HistogramBinTextPainter(Axis2D srcAxis) {
        this.srcAxis = srcAxis;
    }

    public void setHistogramPainter(Array1D array, HistogramPainter painter, DataUnitConverter unitConverter) {
        this.array = array;
        this.unitConverter = unitConverter;
        histPainter = painter;
    }

    @Override
    public void paintTo(GlimpseContext context) {
        if (array == null) {
            return;
        }

        double selected = srcAxis.getAxisX().getSelectionCenter();
        double bin = histPainter.getBin(selected);
        if (bin != lastBin) {
            int count = histPainter.getCount(bin);
            String text = format(bin, histPainter.getBinSize(), count);
            setText(text);

            lastBin = bin;
        }

        super.paintTo(context);
    }

    private String format(double bin, double binWidth, int count) {
        double binLo = unitConverter.painter2data(bin);
        double binHi = unitConverter.painter2data(bin + binWidth);
        return String.format("%s[%d] has %d values in [%s, %s)", array.getName(), array.getSize(), count, binLo, binHi);
    }
}
