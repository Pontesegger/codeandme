package com.codeandme.tableviewer.dnd.sorting;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;

public class DnDSortingSupport {

	public interface IModelManipulator {
		/**
		 * Insert the source object before the target object.
		 * 
		 * @param source source object
		 * @param target target object
		 * @return <code>true</code> when successful
		 */
		boolean insertBefore(Object source, Object target);

		/**
		 * Insert the source object after the target object.
		 * 
		 * @param source source object
		 * @param target target object
		 * @return <code>true</code> when successful
		 */
		boolean insertAfter(Object source, Object target);
	}

	public static class ListDataManipulator<T> implements IModelManipulator {

		private java.util.List<T> fData;

		public ListDataManipulator(java.util.List<T> data) {
			fData = data;
		}

		@Override
		public boolean insertBefore(Object source, Object target) {
			fData.remove(source);
			int targetIndex = fData.indexOf(target);
			if (targetIndex >= 0) {
				fData.add(targetIndex, (T) source);
				return true;
			}

			return false;
		}

		@Override
		public boolean insertAfter(Object source, Object target) {
			fData.remove(source);
			int targetIndex = fData.indexOf(target);
			if (targetIndex >= 0) {
				fData.add(targetIndex + 1, (T) source);
				return true;
			}

			return false;
		}
	}

	/**
	 * Adds DnD support for elements within this table for sorting. Allows to DnD a
	 * single element only.
	 * 
	 * @param tableViewer
	 * @param modelManipulator
	 */
	public static void addDnDSortingSupport(TableViewer tableViewer, IModelManipulator modelManipulator) {

		DragSource dragSource = new DragSource(tableViewer.getControl(), DND.DROP_MOVE);
		dragSource.setTransfer(LocalSelectionTransfer.getTransfer());
		dragSource.addDragListener(new DragSourceAdapter() {

			@Override
			public void dragStart(DragSourceEvent event) {
				event.doit = !tableViewer.getStructuredSelection().isEmpty();
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				if (LocalSelectionTransfer.getTransfer().isSupportedType(event.dataType)) {
					LocalSelectionTransfer.getTransfer().setSelection(tableViewer.getStructuredSelection());
					LocalSelectionTransfer.getTransfer().setSelectionSetTime(event.time & 0xFFFF);
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				LocalSelectionTransfer.getTransfer().setSelection(null);
				LocalSelectionTransfer.getTransfer().setSelectionSetTime(0);
			}
		});

		DropTarget dropTarget = new DropTarget(tableViewer.getControl(), DND.DROP_MOVE);
		dropTarget.setTransfer(LocalSelectionTransfer.getTransfer());
		dropTarget.addDropListener(new ViewerDropAdapter(tableViewer) {

			@Override
			public void dragEnter(DropTargetEvent event) {
				// make sure drag was triggered from current tableViewer
				if (event.widget instanceof DropTarget) {
					boolean isSameViewer = tableViewer.getControl().equals(((DropTarget) event.widget).getControl());
					if (isSameViewer) {
						event.detail = DND.DROP_MOVE;
						setSelectionFeedbackEnabled(false);
						super.dragEnter(event);
					} else
						event.detail = DND.DROP_NONE;
				} else
					event.detail = DND.DROP_NONE;
			}

			@Override
			public boolean validateDrop(Object target, int operation, TransferData transferType) {
				return true;
			}

			@Override
			public boolean performDrop(Object target) {
				int location = determineLocation(getCurrentEvent());
				if (location == LOCATION_BEFORE) {
					if (modelManipulator.insertBefore(getSelectedElement(), getCurrentTarget())) {
						tableViewer.refresh();
						return true;
					}

				} else if (location == LOCATION_AFTER) {
					if (modelManipulator.insertAfter(getSelectedElement(), getCurrentTarget())) {
						tableViewer.refresh();
						return true;
					}
				}

				return false;
			}

			private Object getSelectedElement() {
				return ((IStructuredSelection) LocalSelectionTransfer.getTransfer().getSelection()).getFirstElement();
			}

			/**
			 * Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=545733
			 */
			protected int determineLocation(DropTargetEvent event) {
				if (!(event.item instanceof Item)) {
					return LOCATION_NONE;
				}
				Item item = (Item) event.item;
				Point coordinates = new Point(event.x, event.y);
				coordinates = getViewer().getControl().toControl(coordinates);

				Rectangle bounds = getBounds(item);
				if (bounds == null) {
					return LOCATION_NONE;
				}
				if ((coordinates.y - bounds.y) < getThreshold()) {
					return LOCATION_BEFORE;
				}
				if ((bounds.y + bounds.height - coordinates.y) < getThreshold()) {
					return LOCATION_AFTER;
				}

				return LOCATION_ON;
			}

			/**
			 * Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=545733
			 */
			protected int getThreshold() {
				return ((Table) getViewer().getControl()).getItemHeight() / 2;
			}
		});
	}
}
